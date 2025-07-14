package com.erp.inventory.dto;

import com.erp.inventory.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String id;
    private String email;
    private String fullName;
    private UserRole role;
    private String companyId;
    private boolean active;
    private LocalDateTime createdAt;
}