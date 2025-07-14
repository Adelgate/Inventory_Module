package com.erp.inventory.repository;

import com.erp.inventory.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {
    List<Product> findByCompanyId(String companyId);
}