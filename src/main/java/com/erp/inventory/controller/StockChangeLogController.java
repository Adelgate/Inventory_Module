package com.erp.inventory.controller;

import com.erp.inventory.entity.StockChangeLog;
import com.erp.inventory.security.JwtUtil;
import com.erp.inventory.service.StockChangeLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/stock-change-logs")
@RequiredArgsConstructor
public class StockChangeLogController {
    private final StockChangeLogService stockChangeLogService;
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
    public List<StockChangeLog> getAll(HttpServletRequest request) {
        return stockChangeLogService.findAll(getCurrentCompanyId(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockChangeLog> getById(@PathVariable String id, HttpServletRequest request) {
        return stockChangeLogService.findById(id, getCurrentCompanyId(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StockChangeLog> create(@RequestBody StockChangeLog log, HttpServletRequest request) {
        StockChangeLog saved = stockChangeLogService.save(log, getCurrentCompanyId(request));
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
    public ResponseEntity<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!stockChangeLogService.deleteById(id, getCurrentCompanyId(request))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}