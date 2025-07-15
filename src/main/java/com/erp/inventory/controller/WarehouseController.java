package com.erp.inventory.controller;

import com.erp.inventory.entity.Warehouse;
import com.erp.inventory.security.JwtRequestContext;
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
    private final JwtRequestContext jwtContext;

    @GetMapping
    public List<Warehouse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String address) {
        return warehouseService.findAll(jwtContext.getCompanyId(), page, size, address);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getById(@PathVariable String id) {
        return warehouseService.findById(id, jwtContext.getCompanyId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Warehouse> create(@RequestBody Warehouse warehouse) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        Warehouse saved = warehouseService.save(warehouse, jwtContext.getCompanyId());
        return ResponseEntity.created(URI.create("/api/warehouses/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> update(@PathVariable String id, @RequestBody Warehouse warehouse) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        warehouse.setId(id);
        Warehouse saved = warehouseService.save(warehouse, jwtContext.getCompanyId());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        if (!warehouseService.deleteById(id, jwtContext.getCompanyId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}