package com.erp.inventory.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class CompanyResponse {
    private String id;
    private String name;
    private Map<String, Object> settings;
    private LocalDateTime createdAt;
}