package com.erp.inventory.repository;

import com.erp.inventory.entity.Warehouse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WarehouseRepository extends MongoRepository<Warehouse, String> {
}