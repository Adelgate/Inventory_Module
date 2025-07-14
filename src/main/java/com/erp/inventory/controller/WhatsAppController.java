package com.erp.inventory.controller;

import com.erp.inventory.dto.WhatsAppMessageRequest;
import com.erp.inventory.entity.Product;
import com.erp.inventory.entity.StockChangeLog;
import com.erp.inventory.service.ProductService;
import com.erp.inventory.service.StockChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/whatsapp")
@RequiredArgsConstructor
public class WhatsAppController {
    private final ProductService productService;
    private final StockChangeLogService stockChangeLogService;

    @PostMapping
    public ResponseEntity<?> handleMessage(@RequestBody WhatsAppMessageRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        String userId = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            userId = ((UserDetails) authentication.getPrincipal()).getUsername(); // email
        } else if (authentication.getPrincipal() instanceof String) {
            userId = authentication.getPrincipal().toString();
        }
        // Пример строки: "Краска Белая -1"
        String text = request.getText();
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Text is required");
        }
        String[] parts = text.trim().split(" ");
        if (parts.length < 2) {
            return ResponseEntity.badRequest().body("Invalid format. Example: 'ProductName -1'");
        }
        String amountStr = parts[parts.length - 1];
        int changeAmount;
        try {
            changeAmount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid amount");
        }
        String productName = String.join(" ", java.util.Arrays.copyOf(parts, parts.length - 1));
        Optional<Product> productOpt = productService.findAll().stream()
                .filter(p -> {
                    Object nameObj = p.getData() != null ? p.getData().get("name") : null;
                    return nameObj != null && nameObj.toString().equalsIgnoreCase(productName);
                })
                .findFirst();
        if (productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Product not found: " + productName);
        }
        Product product = productOpt.get();
        Map<String, Object> data = product.getData();
        int currentStock = 0;
        if (data != null && data.get("stock") instanceof Number) {
            currentStock = ((Number) data.get("stock")).intValue();
        }
        int newStock = currentStock + changeAmount;
        if (newStock < 0) {
            return ResponseEntity.badRequest().body("Stock cannot be negative");
        }
        data.put("stock", newStock);
        product.setData(data);
        productService.save(product);
        StockChangeLog log = StockChangeLog.builder()
                .productId(product.getId())
                .userId(userId)
                .warehouseId(product.getWarehouseId())
                .changeAmount(changeAmount)
                .comment("WhatsApp update: " + text)
                .timestamp(LocalDateTime.now())
                .build();
        stockChangeLogService.save(log);
        return ResponseEntity.ok("Stock updated for product: " + productName + ", new stock: " + newStock);
    }
}