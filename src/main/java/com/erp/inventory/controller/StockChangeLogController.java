package com.erp.inventory.controller;

import com.erp.inventory.entity.StockChangeLog;
import com.erp.inventory.security.JwtRequestContext;
import com.erp.inventory.service.StockChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stock-change-logs")
@RequiredArgsConstructor
public class StockChangeLogController {
    private final StockChangeLogService stockChangeLogService;
    private final JwtRequestContext jwtContext;

    @GetMapping
    public List<StockChangeLog> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String productId) {
        return stockChangeLogService.findAll(jwtContext.getCompanyId(), page, size, productId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockChangeLog> getById(@PathVariable String id) {
        return stockChangeLogService.findById(id, jwtContext.getCompanyId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockChangeLog> create(@RequestBody StockChangeLog log) {
        StockChangeLog saved = stockChangeLogService.save(log, jwtContext.getCompanyId());
        return ResponseEntity.created(URI.create("/api/stock-change-logs/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StockChangeLog> update(@PathVariable String id, @RequestBody StockChangeLog log) {
        if (!stockChangeLogService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        log.setId(id);
        return ResponseEntity.ok(stockChangeLogService.save(log));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        if (!stockChangeLogService.deleteById(id, jwtContext.getCompanyId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}