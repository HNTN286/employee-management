package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentStatResponse;
import com.example.employee_management.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping({"", "/summary"})
    public Map<String, Object> summary() {
        return Map.of(
                "totalEmployees", statisticsService.countEmployees(),
                "departmentStats", statisticsService.countByDepartment()
        );
    }

    @GetMapping("/total")
    public Map<String, Long> total() {
        return Map.of("totalEmployees", statisticsService.countEmployees());
    }

    @GetMapping("/by-department")
    public List<DepartmentStatResponse> byDepartment() {
        return statisticsService.countByDepartment();
    }
}