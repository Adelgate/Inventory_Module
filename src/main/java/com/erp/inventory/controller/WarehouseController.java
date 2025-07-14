package com.erp.inventory.controller;

import com.erp.inventory.entity.Warehouse;
import com.erp.inventory.security.JwtUtil;
import com.erp.inventory.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
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
    public List<Warehouse> getAll(HttpServletRequest request) {
        return warehouseService.findAll(getCurrentCompanyId(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getById(@PathVariable String id, HttpServletRequest request) {
        return warehouseService.findById(id, getCurrentCompanyId(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Warehouse> create(@RequestBody Warehouse warehouse, HttpServletRequest request) {
        Warehouse saved = warehouseService.save(warehouse, getCurrentCompanyId(request));
        return ResponseEntity.created(URI.create("/api/warehouses/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> update(@PathVariable String id, @RequestBody Warehouse warehouse,
            HttpServletRequest request) {
        warehouse.setId(id);
        Warehouse saved = warehouseService.save(warehouse, getCurrentCompanyId(request));
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!warehouseService.deleteById(id, getCurrentCompanyId(request))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}