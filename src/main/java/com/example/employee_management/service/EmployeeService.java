package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeRequest;
import com.example.employee_management.dto.EmployeeResponse;
import com.example.employee_management.exception.DuplicateResourceException;
import com.example.employee_management.exception.ResourceNotFoundException;
import com.example.employee_management.model.Department;
import com.example.employee_management.model.Employee;
import com.example.employee_management.repository.DepartmentRepository;
import com.example.employee_management.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            UtilityService utilityService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
    }

    public List<EmployeeResponse> search(String name, String departmentName) {
        String nameFilter = name == null ? "" : name.trim();
        String departmentFilter = departmentName == null ? "" : departmentName.trim();
        return employeeRepository
                .findByNameContainingIgnoreCaseAndDepartmentNameContainingIgnoreCase(
                        nameFilter, departmentFilter)
                .stream()
                .map(EmployeeResponse::from)
                .toList();
    }

    public EmployeeResponse findById(Long id) {
        return EmployeeResponse.from(getEntity(id));
    }

    @CacheEvict(cacheNames = "employeeStats", allEntries = true)
    @Transactional
    public EmployeeResponse create(EmployeeRequest request) {
        if (employeeRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException("Email đã tồn tại: " + request.getEmail());
        }
        Employee employee = new Employee();
        apply(employee, request);
        Employee saved = employeeRepository.save(employee);
        saved.setCode(utilityService.generateEmployeeCode(saved.getId()));
        log.info("Thêm nhân viên: id={}, code={}, email={}",
                saved.getId(), saved.getCode(), saved.getEmail());
        return EmployeeResponse.from(saved);
    }

    @Transactional
    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee employee = getEntity(id);
        if (employeeRepository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("Email đã tồn tại: " + request.getEmail());
        }
        apply(employee, request);
        log.info("Cập nhật nhân viên: id={}", id);
        return EmployeeResponse.from(employee);
    }

    @CacheEvict(cacheNames = "employeeStats", allEntries = true)
    @Transactional
    public void delete(Long id) {
        Employee employee = getEntity(id);
        employeeRepository.delete(employee);
        log.info("Xóa nhân viên: id={}", id);
    }

    private Employee getEntity(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy nhân viên với id = " + id));
    }

    private void apply(Employee employee, EmployeeRequest request) {
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy phòng ban với id = " + request.getDepartmentId()));
        employee.setName(utilityService.formatName(request.getName()));
        employee.setEmail(request.getEmail().trim().toLowerCase());
        employee.setDepartment(department);
    }
}