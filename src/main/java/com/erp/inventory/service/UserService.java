package com.erp.inventory.service;

import com.erp.inventory.dto.UserRequest;
import com.erp.inventory.dto.UserResponse;
import com.erp.inventory.entity.User;
import com.erp.inventory.entity.UserRole;
import com.erp.inventory.mapper.UserMapper;
import com.erp.inventory.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> findAll(String currentCompanyId, int page, int size, String email, String fullName) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll().stream()
                .filter(u -> u.getCompanyId().equals(currentCompanyId))
                .filter(u -> email == null || u.getEmail().toLowerCase().contains(email.toLowerCase()))
                .filter(u -> fullName == null || u.getFullName().toLowerCase().contains(fullName.toLowerCase()))
                .skip((long) page * size)
                .limit(size)
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<UserResponse> findById(String id, String currentCompanyId) {
        return userRepository.findById(id)
                .filter(u -> u.getCompanyId().equals(currentCompanyId))
                .map(userMapper::toResponse);
    }

    public UserResponse create(UserRequest request, String currentCompanyId) {
        if (!request.getCompanyId().equals(currentCompanyId)) {
            throw new SecurityException("Cannot create user for another company");
        }
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User user = userMapper.toEntity(request, hashedPassword);
        return userMapper.toResponse(userRepository.save(user));
    }

    public Optional<UserResponse> update(String id, UserRequest request, String currentCompanyId) {
        return userRepository.findById(id)
                .filter(u -> u.getCompanyId().equals(currentCompanyId))
                .map(user -> {
                    if (!request.getCompanyId().equals(currentCompanyId)) {
                        throw new SecurityException("Cannot move user to another company");
                    }
                    String hashedPassword = passwordEncoder.encode(request.getPassword());
                    userMapper.updateEntity(user, request, hashedPassword);
                    return userMapper.toResponse(userRepository.save(user));
                });
    }

    public boolean deleteById(String id, String currentCompanyId) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent() && userOpt.get().getCompanyId().equals(currentCompanyId)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}