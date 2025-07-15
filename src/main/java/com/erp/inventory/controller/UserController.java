package com.erp.inventory.controller;

import com.erp.inventory.dto.UserRequest;
import com.erp.inventory.dto.UserResponse;
import com.erp.inventory.security.JwtRequestContext;
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
    private final JwtRequestContext jwtContext;

    @GetMapping
    public List<UserResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName) {
        return userService.findAll(jwtContext.getCompanyId(), page, size, email, fullName);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable String id) {
        return userService.findById(id, jwtContext.getCompanyId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        UserResponse saved = userService.create(req, jwtContext.getCompanyId());
        return ResponseEntity.created(URI.create("/api/users/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable String id, @Valid @RequestBody UserRequest req) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        return userService.update(id, req, jwtContext.getCompanyId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!"ADMIN".equals(jwtContext.getUserRole())) {
            return ResponseEntity.status(403).build();
        }
        if (!userService.deleteById(id, jwtContext.getCompanyId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}