package com.example.employee_management.repository;

import com.example.employee_management.dto.DepartmentStatResponse;
import com.example.employee_management.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCaseAndDepartmentNameContainingIgnoreCase(
            String name, String departmentName);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    @Query("SELECT COUNT(e) FROM Employee e")
    long countTotalEmployees();

    @Query("SELECT new com.example.employee_management.dto.DepartmentStatResponse(d.name, COUNT(e)) "
            + "FROM Employee e JOIN e.department d "
            + "GROUP BY d.name ORDER BY COUNT(e) DESC")
    List<DepartmentStatResponse> countByDepartment();
}