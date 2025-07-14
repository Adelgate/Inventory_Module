package com.erp.inventory.mapper;

import com.erp.inventory.dto.CompanyRequest;
import com.erp.inventory.dto.CompanyResponse;
import com.erp.inventory.entity.Company;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CompanyMapper {
    public Company toEntity(CompanyRequest request) {
        return Company.builder()
                .name(request.getName())
                .settings(request.getSettings())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CompanyResponse toResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .settings(company.getSettings())
                .createdAt(company.getCreatedAt())
                .build();
    }

    public void updateEntity(Company company, CompanyRequest request) {
        company.setName(request.getName());
        company.setSettings(request.getSettings());
    }
}