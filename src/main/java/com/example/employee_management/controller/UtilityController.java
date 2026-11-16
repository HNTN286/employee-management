package com.example.employee_management.controller;

import com.example.employee_management.service.UtilityService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/util")
public class UtilityController {

    private final UtilityService utilityService;
    private final PasswordEncoder passwordEncoder;

    public UtilityController(UtilityService utilityService, PasswordEncoder passwordEncoder) {
        this.utilityService = utilityService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/format")
    public String format(@RequestParam String name) {
        return utilityService.formatName(name);
    }

    @GetMapping("/employee-code")
    public String employeeCode() {
        return utilityService.generateEmployeeCode();
    }

    @GetMapping("/encode")
    public Map<String, Object> encode(@RequestParam String raw) {
        String hash = passwordEncoder.encode(raw);
        return Map.of("hash", hash, "matches", passwordEncoder.matches(raw, hash));
    }
}