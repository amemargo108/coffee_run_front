package com.example.demo.controllers;

import com.example.demo.dtos.CreateEmployee;
import com.example.demo.dtos.EmployeeResponse;
import com.example.demo.entities.Employee;
import com.example.demo.services.EmployeeService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //  this is for the runner's view
    @GetMapping("/department/{departmentCode}")
    public ResponseEntity<List<EmployeeResponse>> getByDepartment(@PathVariable String departmentCode) {
        List<EmployeeResponse> response = employeeService.getByDepartment(departmentCode).stream().map(EmployeeResponse::from).toList();
        return ResponseEntity.ok(response);
    }

    //this is for admins only
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@RequestBody CreateEmployee request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(request.getPassword());
        employee.setIs_admin(request.getIsAdmin());
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.create(employee, request.getDepartmentCode())));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable UUID id, @RequestBody CreateEmployee request) {
        Employee employee = new Employee();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(request.getPassword());
        employee.setIs_admin(request.getIsAdmin());
        return ResponseEntity.ok(EmployeeResponse.from(employeeService.update(id, employee)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeResponse>> getAll() {
        return ResponseEntity.ok(employeeService.getAll().stream().map(EmployeeResponse::from).toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
