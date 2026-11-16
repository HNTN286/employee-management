package com.example.employee_management.service;

import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.model.Department;
import com.example.employee_management.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Transactional
    public Department create(String name) {
        if (departmentRepository.existsByNameIgnoreCase(name)) {
            throw new DuplicateResourceException("Phòng ban đã tồn tại: " + name);
        }
        return departmentRepository.save(new Department(name.trim()));
    }
}