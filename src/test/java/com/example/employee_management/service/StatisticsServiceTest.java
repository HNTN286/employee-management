package com.example.employee_management.service;

import com.example.employee_management.dto.DepartmentStatResponse;
import com.example.employee_management.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private StatisticsService statisticsService;

    @Test
    void countEmployees_shouldReturnTotalCountFromRepository() {
        when(employeeRepository.countTotalEmployees()).thenReturn(5L);

        long count = statisticsService.countEmployees();

        assertEquals(5L, count);
        verify(employeeRepository).countTotalEmployees();
    }

    @Test
    void countByDepartment_shouldReturnDepartmentStatsFromRepository() {
        List<DepartmentStatResponse> mockStats = List.of(
                new DepartmentStatResponse("IT", 3L),
                new DepartmentStatResponse("HR", 2L)
        );
        when(employeeRepository.countByDepartment()).thenReturn(mockStats);

        List<DepartmentStatResponse> result = statisticsService.countByDepartment();

        assertEquals(2, result.size());
        assertEquals("IT", result.get(0).getDepartment());
        assertEquals(3L, result.get(0).getTotal());
        assertEquals("HR", result.get(1).getDepartment());
        assertEquals(2L, result.get(1).getTotal());
        verify(employeeRepository).countByDepartment();
    }
}
