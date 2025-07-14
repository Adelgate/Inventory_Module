package com.erp.inventory.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private String role;
    private boolean active;
    private String companyId;
    private LocalDateTime createdAt;
}