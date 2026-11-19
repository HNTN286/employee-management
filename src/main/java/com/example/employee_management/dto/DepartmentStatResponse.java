package com.example.employee_management.dto;

public class DepartmentStatResponse {

    private final String department;
    private final Long total;

    public DepartmentStatResponse(String department, Long total) {
        this.department = department;
        this.total = total;
    }

    public String getDepartment() {
        return department;
    }

    public Long getTotal() {
        return total;
    }
}