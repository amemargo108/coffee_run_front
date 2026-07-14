package com.example.demo.repository;

import com.example.demo.entities.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, String> {
Optional<Department> findByCode(String code);
}
