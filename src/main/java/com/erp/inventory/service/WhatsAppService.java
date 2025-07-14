package com.erp.inventory.service;

import com.erp.inventory.entity.Product;
import com.erp.inventory.entity.StockChangeLog;
import com.erp.inventory.repository.ProductRepository;
import com.erp.inventory.repository.StockChangeLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {

    private final ProductRepository productRepository;
    private final StockChangeLogRepository stockChangeLogRepository;

    // Паттерн для парсинга сообщений вида "Краска Белая -1" или "Краска Белая +5"
    private static final Pattern MESSAGE_PATTERN = Pattern.compile("(.+?)\\s*([+-]\\d+)$");

    public void processMessage(String message, String companyId, String userId) {
        log.info("Processing WhatsApp message: {}", message);

        Matcher matcher = MESSAGE_PATTERN.matcher(message.trim());
        if (!matcher.matches()) {
            throw new RuntimeException("Invalid message format. Expected: 'Product Name +/-amount'");
        }

        String productName = matcher.group(1).trim();
        String changeStr = matcher.group(2);
        int changeAmount = Integer.parseInt(changeStr);

        // Ищем продукт по названию в компании
        List<Product> products = productRepository.findByCompanyId(companyId);
        Product product = products.stream()
                .filter(p -> {
                    Map<String, Object> data = p.getData();
                    String name = (String) data.get("name");
                    return name != null && name.equalsIgnoreCase(productName);
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found: " + productName));

        // Обновляем остаток
        Map<String, Object> data = product.getData();
        Integer currentStock = (Integer) data.getOrDefault("stock", 0);
        Integer newStock = currentStock + changeAmount;

        if (newStock < 0) {
            throw new RuntimeException("Stock cannot be negative. Current: " + currentStock + ", trying to subtract: "
                    + Math.abs(changeAmount));
        }

        data.put("stock", newStock);
        product.setData(data);
        productRepository.save(product);

        // Создаем запись в логе
        StockChangeLog logEntry = StockChangeLog.builder()
                .productId(product.getId())
                .userId(userId)
                .warehouseId(product.getWarehouseId())
                .changeAmount(changeAmount)
                .comment("WhatsApp message: " + message)
                .build();

        stockChangeLogRepository.save(logEntry);

        log.info("Stock updated for product '{}': {} -> {} (change: {})",
                productName, currentStock, newStock, changeAmount);
    }
}