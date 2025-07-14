package com.erp.inventory.mapper;

import com.erp.inventory.dto.UserRequest;
import com.erp.inventory.dto.UserResponse;
import com.erp.inventory.entity.User;
import com.erp.inventory.entity.UserRole;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public User toEntity(UserRequest request, String hashedPassword) {
        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(hashedPassword)
                .role(UserRole.valueOf(request.getRole()))
                .companyId(request.getCompanyId())
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .active(user.isActive())
                .companyId(user.getCompanyId())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public void updateEntity(User user, UserRequest request, String hashedPassword) {
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(UserRole.valueOf(request.getRole()));
        user.setCompanyId(request.getCompanyId());
    }
}