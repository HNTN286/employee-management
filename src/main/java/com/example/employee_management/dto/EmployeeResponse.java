package com.example.employee_management.dto;

import com.example.employee_management.model.Employee;

public class EmployeeResponse {

    private final Long id;
    private final String code;
    private final String name;
    private final String email;
    private final Long departmentId;
    private final String departmentName;

    public EmployeeResponse(Long id, String code, String name, String email,
            Long departmentId, String departmentName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public static EmployeeResponse from(Employee e) {
        return new EmployeeResponse(e.getId(), e.getCode(), e.getName(), e.getEmail(),
                e.getDepartment().getId(), e.getDepartment().getName());
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}