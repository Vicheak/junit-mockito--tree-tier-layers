package com.vicheak.unittestdemo.service;

import com.vicheak.unittestdemo.entity.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

    List<Employee> loadEmployees();

    Optional<Employee> loadEmployeeById(Long id);

    Employee createNewEmployee(Employee employee);

    Employee updateEmployee(Employee employee);

    void deleteEmployee(Long id);

}
