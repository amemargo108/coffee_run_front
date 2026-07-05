package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.entities.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.repository.DepartmentRepository;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final DepartmentRepository departmentRepository;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
    }

    public List<Employee> getByDepartment(String departmentCode) {
        return employeeRepository.findByDepartmentCode(departmentCode);
    }
    //this allows the admins to set up employees initial password
    @PreAuthorize("hasRole('ADMIN')")
    public Employee create(Employee employee, String departmentCode) {
        Department department = departmentRepository.findById(departmentCode).orElseThrow(() -> new RuntimeException("This department could not be found: " + departmentCode));
        employee.setDepartment(department);
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return employeeRepository.save(employee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Employee update(UUID id, Employee updated) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This employee could not be found " + id));

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setIs_admin(updated.getIs_admin());

        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(passwordEncoder.encode(updated.getPassword()));
        }

        return employeeRepository.save(existing);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        employeeRepository.deleteById(id);
    }

}
