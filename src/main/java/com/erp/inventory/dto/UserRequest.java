package com.erp.inventory.dto;

import com.erp.inventory.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "must not be blank")
    private String fullName;
    @NotBlank(message = "must not be blank")
    @Email(message = "must be a valid email")
    private String email;
    @NotBlank(message = "must not be blank")
    private String password;
    @NotBlank(message = "must not be blank")
    private String role;
    @NotBlank(message = "must not be blank")
    private String companyId;
}