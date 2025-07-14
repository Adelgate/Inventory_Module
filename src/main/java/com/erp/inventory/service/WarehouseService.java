package com.erp.inventory.service;

import com.erp.inventory.entity.Warehouse;
import com.erp.inventory.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public List<Warehouse> findAll(String currentCompanyId) {
        return warehouseRepository.findAll().stream()
                .filter(w -> w.getCompanyId().equals(currentCompanyId))
                .collect(Collectors.toList());
    }

    public Optional<Warehouse> findById(String id, String currentCompanyId) {
        return warehouseRepository.findById(id)
                .filter(w -> w.getCompanyId().equals(currentCompanyId));
    }

    public Warehouse save(Warehouse warehouse, String currentCompanyId) {
        if (!warehouse.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Cannot create/update warehouse for another company");
        }
        return warehouseRepository.save(warehouse);
    }

    public boolean deleteById(String id, String currentCompanyId) {
        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(id);
        if (warehouseOpt.isPresent() && warehouseOpt.get().getCompanyId().equals(currentCompanyId)) {
            warehouseRepository.deleteById(id);
            return true;
        }
        return false;
    }
}