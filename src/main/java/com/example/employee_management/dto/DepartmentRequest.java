package com.example.employee_management.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentRequest(
        @NotBlank(message = "Tên phòng ban không được để trống") String name) {
}