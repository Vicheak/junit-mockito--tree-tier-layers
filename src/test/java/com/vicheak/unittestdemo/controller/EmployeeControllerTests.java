package com.vicheak.unittestdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vicheak.unittestdemo.entity.Employee;
import com.vicheak.unittestdemo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testLoadEmployees() throws Exception {
        //given
        List<Employee> employeeList = new ArrayList<>() {{
            add(Employee.builder()
                    .id(1L)
                    .name("jackson")
                    .email("jackson@gmail.com")
                    .build());
            add(Employee.builder()
                    .id(2L)
                    .name("dara")
                    .email("dara@gmail.com")
                    .build());
        }};
        given(employeeService.loadEmployees())
                .willReturn(employeeList);

        //when
        ResultActions response = mockMvc.perform(get("/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(employeeList.size())));
    }

    @Test
    public void testLoadEmployeesEmpty() throws Exception {
        //given
        given(employeeService.loadEmployees())
                .willReturn(List.of());

        //when
        ResultActions response = mockMvc.perform(get("/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(0)));
    }

    @Test
    public void testLoadEmployeeById() throws Exception {
        //given
        Employee employee = Employee.builder()
                .id(1L)
                .name("dara")
                .email("dara@gmail.com")
                .build();
        given(employeeService.loadEmployeeById(1L))
                .willReturn(Optional.of(employee));

        //when
        ResultActions response = mockMvc.perform(get("/employees/{id}", 1L));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void testLoadEmployeeByIdThrow() throws Exception {
        //given
        given(employeeService.loadEmployeeById(1L))
                .willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(get("/employees/{id}", 1L));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateNewEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .name("dara")
                .email("dara@gmail.com")
                .build();
        given(employeeService.createNewEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(employee.getName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        //given
        Employee employee = Employee.builder()
                .name("jackson")
                .email("jackson@gmail.com")
                .build();
        Employee updatedEmployee = Employee.builder()
                .name("dara")
                .email("dara@gmail.com")
                .build();

        given(employeeService.loadEmployeeById(1L))
                .willReturn(Optional.of(employee));
        given(employeeService.updateEmployee(employee))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(put("/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(updatedEmployee.getName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @Test
    public void testUpdateEmployeeThrow() throws Exception {
        //given
        Employee updatedEmployee = Employee.builder()
                .name("dara")
                .email("dara@gmail.com")
                .build();

        given(employeeService.loadEmployeeById(1L))
                .willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(put("/employees/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        //given
        willDoNothing().given(employeeService).deleteEmployee(1L);

        //when
        ResultActions response = mockMvc.perform(delete("/employees/{id}", 1L));

        //then
        response.andDo(print())
                .andExpect(status().isNoContent());
    }

}
