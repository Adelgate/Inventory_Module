package com.erp.inventory.repository;

import com.erp.inventory.entity.StockChangeLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StockChangeLogRepository extends MongoRepository<StockChangeLog, String> {
}