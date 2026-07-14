package com.example.demo.services;

import com.example.demo.entities.Department;
import com.example.demo.repository.DepartmentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Department create(Department department) {
        return departmentRepository.save(department);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Department update(String code, Department updated) {
        Department existing = departmentRepository.findById(code).orElseThrow(() -> new RuntimeException("Department not found: " + code));
        existing.setName(updated.getName());
        return departmentRepository.save(existing);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String code) {
        departmentRepository.deleteById(code);
    }
}
