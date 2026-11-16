package com.example.employee_management.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UtilityService {

    private final AtomicInteger counter = new AtomicInteger(0);

    /** "  nguyen   van a " -> "Nguyen Van A" */
    public String formatName(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return Arrays.stream(raw.trim().toLowerCase().split("\\s+"))
                .map(w -> Character.toUpperCase(w.charAt(0)) + w.substring(1))
                .collect(Collectors.joining(" "));
    }

    /** Sinh mã nhân viên tự động: EMP001, EMP002, ... */
    public String generateEmployeeCode() {
        return String.format("EMP%03d", counter.incrementAndGet());
    }
}