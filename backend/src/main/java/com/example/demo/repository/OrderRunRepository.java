package com.example.demo.repository;

import com.example.demo.entities.OrderRun;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRunRepository extends JpaRepository<OrderRun, UUID> {
    List<OrderRun> findByRunnerId(UUID runnerId);
    List<OrderRun> findByDepartmentCode(String departmentCode);
    List<OrderRun> findByPulledAtBetween(LocalDateTime start, LocalDateTime end);
    List<OrderRun> findByRunnerIdAndPulledAtBetween(UUID runnerId, LocalDateTime start, LocalDateTime end);
    List<OrderRun> findByDepartmentCodeAndPulledAtBetween(String departmentCode, LocalDateTime start, LocalDateTime end);
}
