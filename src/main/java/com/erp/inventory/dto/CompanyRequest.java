package com.erp.inventory.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

@Data
public class CompanyRequest {
    @NotBlank(message = "must not be blank")
    private String name;
    private Map<String, Object> settings;
}