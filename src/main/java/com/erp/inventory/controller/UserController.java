package com.erp.inventory.controller;

import com.erp.inventory.dto.UserRequest;
import com.erp.inventory.dto.UserResponse;
import com.erp.inventory.security.JwtUtil;
import com.erp.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    private String getCurrentCompanyId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractCompanyId(token);
        }
        return null;
    }

    private String getCurrentUserRole(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return jwtUtil.extractUserRole(token);
        }
        return null;
    }

    @GetMapping
    public List<UserResponse> getAll(HttpServletRequest request) {
        return userService.findAll(getCurrentCompanyId(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id, HttpServletRequest request) {
        return userService.findById(id, getCurrentCompanyId(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req, HttpServletRequest request) {
        if (!"ADMIN".equals(getCurrentUserRole(request))) {
            return ResponseEntity.status(403).build();
        }
        UserResponse saved = userService.create(req, getCurrentCompanyId(request));
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest req,
            HttpServletRequest request) {
        if (!"ADMIN".equals(getCurrentUserRole(request))) {
            return ResponseEntity.status(403).build();
        }
        return userService.update(id, req, getCurrentCompanyId(request))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, HttpServletRequest request) {
        if (!"ADMIN".equals(getCurrentUserRole(request))) {
            return ResponseEntity.status(403).build();
        }
        if (!userService.deleteById(id, getCurrentCompanyId(request))) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}