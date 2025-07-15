package com.erp.inventory.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtRequestContext {
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    private String getToken() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public String getCompanyId() {
        String token = getToken();
        return token != null ? jwtUtil.extractCompanyId(token) : null;
    }

    public String getUserRole() {
        String token = getToken();
        return token != null ? jwtUtil.extractUserRole(token) : null;
    }

    public String getUserEmail() {
        String token = getToken();
        return token != null ? jwtUtil.extractUserEmail(token) : null;
    }
}