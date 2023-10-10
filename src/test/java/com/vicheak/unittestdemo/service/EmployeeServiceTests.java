package com.vicheak.unittestdemo.service;

import com.vicheak.unittestdemo.entity.Employee;
import com.vicheak.unittestdemo.exception.ResourceDuplicatedException;
import com.vicheak.unittestdemo.repository.EmployeeRepository;
import com.vicheak.unittestdemo.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .name("jackson")
                .email("jackson@gmail.com")
                .build();
    }

    @Test
    public void testLoadEmployees() {
        //given
        given(employeeRepository.findAll())
                .willReturn(List.of(employee));

        //when
        List<Employee> employeeList = employeeService.loadEmployees();

        //then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertEquals(1, employeeList.size());
    }

    @Test
    public void testLoadEmployeesEmpty(){
        //given
        given(employeeRepository.findAll())
                .willReturn(List.of());

        //when
        List<Employee> employeeList = employeeService.loadEmployees();

        //then
        assertThat(employeeList).isEmpty();
    }

    @Test
    public void testLoadEmployeeById() {
        //given
        given(employeeRepository.findById(1L))
                .willReturn(Optional.of(employee));

        //when
        Employee employeeReturn = employeeService.loadEmployeeById(1L).get();

        //then
        assertThat(employeeReturn).isNotNull();
        assertEquals(1, employeeReturn.getId());
    }

    @Test
    public void testLoadEmployeeByIdThrow() {
        //given
        given(employeeRepository.findById(1L))
                .willReturn(Optional.empty());

        //when
        Optional<Employee> employeeOptional = employeeService.loadEmployeeById(1L);
        Employee employeeReturn = employeeOptional.orElse(null);

        //then
        assertThat(employeeReturn).isNull();
    }

    @Test
    public void testCreateNewEmployee() {
        //given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());
        given(employeeRepository.save(employee))
                .willReturn(employee);

        //when
        Employee employeeReturn = employeeService.createNewEmployee(employee);

        //then
        assertThat(employeeReturn).isNotNull();
    }

    @Test
    public void testCreateNewEmployeeThrow() {
        //given
        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //when
        assertThrows(ResourceDuplicatedException.class,
                () -> employeeService.createNewEmployee(employee));

        //then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    public void testUpdateEmployee() {
        //given
        given(employeeRepository.save(employee))
                .willReturn(employee);
        employee.setName(employee.getName() + "s");

        //when
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        //then
        assertThat(updatedEmployee.getName()).isEqualTo("jacksons");
    }

    @Test
    public void testDeleteEmployee(){
        //given
        willDoNothing().given(employeeRepository).deleteById(1L);

        //when
        employeeService.deleteEmployee(1L);

        //then
        verify(employeeRepository, times(1)).deleteById(1L);
    }

}
