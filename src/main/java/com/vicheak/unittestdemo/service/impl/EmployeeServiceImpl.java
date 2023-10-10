package com.vicheak.unittestdemo.service.impl;

import com.vicheak.unittestdemo.entity.Employee;
import com.vicheak.unittestdemo.exception.ResourceDuplicatedException;
import com.vicheak.unittestdemo.repository.EmployeeRepository;
import com.vicheak.unittestdemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<Employee> loadEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> loadEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee createNewEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());
        if (savedEmployee.isPresent()) {
            throw new ResourceDuplicatedException("Duplicated resource!");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
