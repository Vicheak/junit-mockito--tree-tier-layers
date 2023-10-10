package com.vicheak.unittestdemo.controller;

import com.vicheak.unittestdemo.entity.Employee;
import com.vicheak.unittestdemo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> loadEmployees() {
        return ResponseEntity.ok(employeeService.loadEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> loadEmployeeById(@PathVariable("id") Long id) {
        return employeeService.loadEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee createNewEmployee(@RequestBody Employee employee) {
        return employeeService.createNewEmployee(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable("id") Long id,
                                            @RequestBody Employee employee) {
        return employeeService.loadEmployeeById(id)
                .map(savedEmployee -> {
                    savedEmployee.setName(employee.getName());
                    savedEmployee.setEmail(employee.getEmail());
                    return ResponseEntity.ok(employeeService.updateEmployee(savedEmployee));
                }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>("Employee delete successfully!", HttpStatus.NO_CONTENT);
    }

}
