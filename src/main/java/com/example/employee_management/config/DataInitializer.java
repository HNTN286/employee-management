package com.example.employee_management.config;

import com.example.employee_management.model.Department;
import com.example.employee_management.model.Role;
import com.example.employee_management.model.User;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(DepartmentRepository departmentRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            departmentRepository.saveAll(List.of(
                    new Department("IT"),
                    new Department("HR"),
                    new Department("Finance")));
        }
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", passwordEncoder.encode("admin123"), Role.ADMIN));
        }
        if (!userRepository.existsByUsername("user")) {
            userRepository.save(new User("user", passwordEncoder.encode("user123"), Role.USER));
        }
    }
}