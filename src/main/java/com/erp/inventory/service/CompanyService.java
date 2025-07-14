package com.erp.inventory.service;

import com.erp.inventory.dto.CompanyRequest;
import com.erp.inventory.dto.CompanyResponse;
import com.erp.inventory.entity.Company;
import com.erp.inventory.mapper.CompanyMapper;
import com.erp.inventory.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;

    public List<CompanyResponse> findAll() {
        return companyRepository.findAll().stream()
                .map(companyMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<CompanyResponse> findById(String id) {
        return companyRepository.findById(id).map(companyMapper::toResponse);
    }

    public CompanyResponse create(CompanyRequest request) {
        Company company = companyMapper.toEntity(request);
        return companyMapper.toResponse(companyRepository.save(company));
    }

    public Optional<CompanyResponse> update(String id, CompanyRequest request) {
        return companyRepository.findById(id).map(company -> {
            companyMapper.updateEntity(company, request);
            return companyMapper.toResponse(companyRepository.save(company));
        });
    }

    public boolean deleteById(String id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        }
        return false;
    }
}