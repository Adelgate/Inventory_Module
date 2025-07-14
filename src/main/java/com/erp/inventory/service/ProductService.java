package com.erp.inventory.service;

import com.erp.inventory.entity.Product;
import com.erp.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> findAll(String currentCompanyId) {
        return productRepository.findAll().stream()
                .filter(p -> p.getCompanyId().equals(currentCompanyId))
                .collect(Collectors.toList());
    }

    public Optional<Product> findById(String id, String currentCompanyId) {
        return productRepository.findById(id)
                .filter(p -> p.getCompanyId().equals(currentCompanyId));
    }

    public Product save(Product product, String currentCompanyId) {
        if (!product.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Cannot create/update product for another company");
        }
        return productRepository.save(product);
    }

    public boolean deleteById(String id, String currentCompanyId) {
        Optional<Product> productOpt = productRepository.findById(id);
        if (productOpt.isPresent() && productOpt.get().getCompanyId().equals(currentCompanyId)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
}