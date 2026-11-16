package com.example.employee_management.config;

import com.example.employee_management.model.Department;
import com.example.employee_management.repository.DepartmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;

    public DataInitializer(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            departmentRepository.saveAll(List.of(
                    new Department("IT"),
                    new Department("HR"),
                    new Department("Finance")));
        }
    }
}