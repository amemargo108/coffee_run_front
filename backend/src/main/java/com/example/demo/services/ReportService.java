package com.example.demo.services;

import com.example.demo.entities.OrderRun;
import com.example.demo.entities.OrderRunItem;
import com.example.demo.repository.OrderRunItemRepository;
import com.example.demo.repository.OrderRunRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ReportService {

    private final OrderRunRepository orderRunRepository;
    private final OrderRunItemRepository orderRunItemRepository;

    public ReportService(OrderRunRepository orderRunRepository, OrderRunItemRepository orderRunItemRepository) {
        this.orderRunRepository = orderRunRepository;
        this.orderRunItemRepository = orderRunItemRepository;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderRun> getRunsByRunner(UUID runnerId) {
            return orderRunRepository.findByRunnerId(runnerId);
        }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderRun> getRunsByDepartment(String departmentCode) {
        return orderRunRepository.findByDepartmentCode(departmentCode);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderRun> getRunsByDateRange(LocalDateTime start, LocalDateTime end) {
        return orderRunRepository.findByPulledAtBetween(start, end);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderRun> getRunsByDepartmentAndDateRange(String departmentId, LocalDateTime start, LocalDateTime end) {
        return orderRunRepository.findByDepartmentCodeAndPulledAtBetween(departmentId, start, end);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderRun> getRunsByRunnerAndDateRange(UUID runnerId, LocalDateTime start, LocalDateTime end) {
        return orderRunRepository.findByRunnerIdAndPulledAtBetween(runnerId, start, end);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderRunItem> getItemsForRun(UUID orderRunId) {
        return orderRunItemRepository.findByOrderRunId(orderRunId);
    }

}
