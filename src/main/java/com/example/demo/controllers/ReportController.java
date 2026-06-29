package com.example.demo.controllers;

import com.example.demo.entities.OrderRun;
import com.example.demo.entities.OrderRunItem;
import com.example.demo.services.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/runner/{runnerId}")
    public ResponseEntity<List<OrderRun>> getByRunner(@PathVariable UUID runnerId) {
        return ResponseEntity.ok(reportService.getRunsByRunner(runnerId));
    }

    @GetMapping("/department/{departmentCode}")
    public ResponseEntity<List<OrderRun>> getByDepartment(@PathVariable String departmentCode) {
        return ResponseEntity.ok(reportService.getRunsByDepartment(departmentCode));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderRun>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getRunsByDateRange(start, end));
    }

    @GetMapping("/runner/{runnerId}/date-range")
    public ResponseEntity<List<OrderRun>> getByRunnerAndDateRange(
            @PathVariable UUID runnerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getRunsByRunnerAndDateRange(runnerId, start, end));
    }

    @GetMapping("/department/{departmentCode}/date-range")
    public ResponseEntity<List<OrderRun>> getByDepartmentAndDateRange(
            @PathVariable String departmentCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getRunsByDepartmentAndDateRange(departmentCode, start, end));
    }

    @GetMapping("/runs/{orderRunId}/items")
    public ResponseEntity<List<OrderRunItem>> getItemsForRun(@PathVariable UUID orderRunId) {
        return ResponseEntity.ok(reportService.getItemsForRun(orderRunId));
    }

}
