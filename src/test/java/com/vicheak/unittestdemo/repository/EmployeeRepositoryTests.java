package com.vicheak.unittestdemo.repository;

import com.vicheak.unittestdemo.entity.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setup() {
        Employee employee = Employee.builder()
                .name("jackson")
                .email("jackson@gmail.com")
                .build();
        employeeRepository.save(employee);
    }

    @Test
    public void testFindByEmail() {
        //given

        //when
        Optional<Employee> employeeOptional = employeeRepository.findByEmail("jackson@gmail.com");
        Employee employeeReturn = employeeOptional.orElse(null);

        //then
        assertNotNull(employeeReturn);
    }

    @Test
    public void testFindByEmailThrow() {
        //given

        //when
        Optional<Employee> employeeOptional = employeeRepository.findByEmail("dara@gmail.com");
        Employee employeeReturn = employeeOptional.orElse(null);

        //then
        assertNull(employeeReturn);
    }

}
