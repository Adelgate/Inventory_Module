package com.erp.inventory.controller;

import com.erp.inventory.entity.Product;
import com.erp.inventory.security.JwtRequestContext;
import com.erp.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final JwtRequestContext jwtContext;

    @GetMapping
    public List<Product> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String name) {
        return productService.findAll(jwtContext.getCompanyId(), page, size, name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable String id) {
        return productService.findById(id, jwtContext.getCompanyId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        Product saved = productService.save(product, jwtContext.getCompanyId());
        return ResponseEntity.created(URI.create("/api/products/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(@PathVariable String id, @RequestBody Product product) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        product.setId(id);
        Product saved = productService.save(product, jwtContext.getCompanyId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        if (!productService.deleteById(id, jwtContext.getCompanyId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}