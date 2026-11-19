package com.example.employee_management.controller;

import com.example.employee_management.dto.DepartmentStatResponse;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    @InjectMocks
    private StatisticsController statisticsController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(statisticsController).build();
    }

    @Test
    void getTotal_shouldReturnTotalEmployeesMap() throws Exception {
        when(statisticsService.countEmployees()).thenReturn(15L);

        mockMvc.perform(get("/api/statistics/total"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEmployees").value(15));

        verify(statisticsService).countEmployees();
    }

    @Test
    void getByDepartment_shouldReturnDepartmentList() throws Exception {
        List<DepartmentStatResponse> mockList = List.of(
                new DepartmentStatResponse("IT", 10L),
                new DepartmentStatResponse("HR", 5L)
        );
        when(statisticsService.countByDepartment()).thenReturn(mockList);

        mockMvc.perform(get("/api/statistics/by-department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].department").value("IT"))
                .andExpect(jsonPath("$[0].total").value(10))
                .andExpect(jsonPath("$[1].department").value("HR"))
                .andExpect(jsonPath("$[1].total").value(5));

        verify(statisticsService).countByDepartment();
    }

    @Test
    void getSummary_shouldReturnCombinedData() throws Exception {
        when(statisticsService.countEmployees()).thenReturn(8L);
        when(statisticsService.countByDepartment()).thenReturn(List.of(
                new DepartmentStatResponse("Finance", 8L)
        ));

        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalEmployees").value(8))
                .andExpect(jsonPath("$.departmentStats[0].department").value("Finance"))
                .andExpect(jsonPath("$.departmentStats[0].total").value(8));
    }
}
