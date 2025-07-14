package com.erp.inventory.controller;

import com.erp.inventory.entity.Product;
import com.erp.inventory.security.JwtUtil;
import com.erp.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final JwtUtil jwtUtil;

    private String getCurrentCompanyId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractCompanyId(token);
        }
        return null;
    }

    @GetMapping
    public List<Product> getAll(HttpServletRequest request) {
        return productService.findAll(getCurrentCompanyId(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id, HttpServletRequest request) {
        return productService.findById(id, getCurrentCompanyId(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product, HttpServletRequest request) {
        Product saved = productService.save(product, getCurrentCompanyId(request));
        return ResponseEntity.created(URI.create("/api/products/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product,
            HttpServletRequest request) {
        product.setId(id);
        Product saved = productService.save(product, getCurrentCompanyId(request));
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!productService.deleteById(id, getCurrentCompanyId(request))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}