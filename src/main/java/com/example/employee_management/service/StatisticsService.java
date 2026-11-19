package com.example.employee_management.service;

import com.example.employee_management.dto.DepartmentStatResponse;
import com.example.employee_management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    private static final Logger log = LoggerFactory.getLogger(StatisticsService.class);

    private final EmployeeRepository employeeRepository;

    public StatisticsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Cacheable("employeeStats")
    public long countEmployees() {
        log.info("Đếm nhân viên từ database (cache miss)");
        return employeeRepository.countTotalEmployees();
    }

    public List<DepartmentStatResponse> countByDepartment() {
        return employeeRepository.countByDepartment();
    }
}