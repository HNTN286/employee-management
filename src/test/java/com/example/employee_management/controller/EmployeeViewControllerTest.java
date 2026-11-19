package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentStatResponse;
import com.example.employee_management.service.DepartmentService;
import com.example.employee_management.service.EmployeeService;
import com.example.employee_management.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(MockitoExtension.class)
class EmployeeViewControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private DepartmentService departmentService;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private EmployeeViewController employeeViewController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeViewController).build();
    }

    @Test
    void statistics_shouldPopulateModelAndReturnViewName() throws Exception {
        when(statisticsService.countEmployees()).thenReturn(12L);
        when(statisticsService.countByDepartment()).thenReturn(List.of(
                new DepartmentStatResponse("IT", 8L),
                new DepartmentStatResponse("HR", 4L)
        ));

        mockMvc.perform(get("/employees/statistics"))
                .andExpect(status().isOk())
                .andExpect(view().name("employees/statistics"))
                .andExpect(model().attribute("totalEmployees", 12L))
                .andExpect(model().attributeExists("departmentStats"));

        verify(statisticsService).countEmployees();
        verify(statisticsService).countByDepartment();
    }
}
