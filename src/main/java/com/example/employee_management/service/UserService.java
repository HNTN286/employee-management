package com.example.employee_management.service;

import com.example.employee_management.dto.RegisterRequest;
import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.model.Role;
import com.example.employee_management.model.User;
import com.example.employee_management.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest request) {
        String username = request.getUsername().trim();
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username đã tồn tại: " + username);
        }
        // Đăng ký công khai chỉ tạo USER, tài khoản ADMIN do hệ thống tạo sẵn
        return userRepository.save(
                new User(username, passwordEncoder.encode(request.getPassword()), Role.USER));
    }
}