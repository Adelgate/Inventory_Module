package com.erp.inventory.service;

import com.erp.inventory.entity.StockChangeLog;
import com.erp.inventory.entity.Product;
import com.erp.inventory.repository.StockChangeLogRepository;
import com.erp.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockChangeLogService {
    private final StockChangeLogRepository stockChangeLogRepository;
    private final ProductRepository productRepository;

    public List<StockChangeLog> findAll(String currentCompanyId) {
        return stockChangeLogRepository.findAll().stream()
                .filter(log -> {
                    Optional<Product> productOpt = productRepository.findById(log.getProductId());
                    return productOpt.isPresent() && productOpt.get().getCompanyId().equals(currentCompanyId);
                })
                .collect(Collectors.toList());
    }

    public Optional<StockChangeLog> findById(String id, String currentCompanyId) {
        return stockChangeLogRepository.findById(id)
                .filter(log -> {
                    Optional<Product> productOpt = productRepository.findById(log.getProductId());
                    return productOpt.isPresent() && productOpt.get().getCompanyId().equals(currentCompanyId);
                });
    }

    public StockChangeLog save(StockChangeLog log, String currentCompanyId) {
        Optional<Product> productOpt = productRepository.findById(log.getProductId());
        if (productOpt.isEmpty() || !productOpt.get().getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Cannot create log for another company");
        }
        return stockChangeLogRepository.save(log);
    }

    public boolean deleteById(String id, String currentCompanyId) {
        Optional<StockChangeLog> logOpt = stockChangeLogRepository.findById(id);
        if (logOpt.isPresent()) {
            Optional<Product> productOpt = productRepository.findById(logOpt.get().getProductId());
            if (productOpt.isPresent() && productOpt.get().getCompanyId().equals(currentCompanyId)) {
                stockChangeLogRepository.deleteById(id);
                return true;
            }
        }
        return false;
    }
}