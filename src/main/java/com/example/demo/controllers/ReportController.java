package com.example.demo.controllers;

import com.example.demo.dtos.OrderRunItemResponse;
import com.example.demo.dtos.OrderRunResponse;
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
    public ResponseEntity<List<OrderRunResponse>> getByRunner(@PathVariable UUID runnerId) {
        return ResponseEntity.ok(reportService.getRunsByRunner(runnerId).stream()
                .map(run -> OrderRunResponse.from(run, getItems(run))).toList());
    }

    @GetMapping("/department/{departmentCode}")
    public ResponseEntity<List<OrderRunResponse>> getByDepartment(@PathVariable String departmentCode) {
        return ResponseEntity.ok(reportService.getRunsByDepartment(departmentCode).stream()
                .map(run -> OrderRunResponse.from(run, getItems(run))).toList());
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderRunResponse>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getRunsByDateRange(start, end).stream()
                .map(run -> OrderRunResponse.from(run, getItems(run))).toList());
    }

    @GetMapping("/runner/{runnerId}/date-range")
    public ResponseEntity<List<OrderRunResponse>> getByRunnerAndDateRange(
            @PathVariable UUID runnerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getRunsByRunnerAndDateRange(runnerId, start, end).stream()
                .map(run -> OrderRunResponse.from(run, getItems(run))).toList());
    }

    @GetMapping("/department/{departmentCode}/date-range")
    public ResponseEntity<List<OrderRunResponse>> getByDepartmentAndDateRange(
            @PathVariable String departmentCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(reportService.getRunsByDepartmentAndDateRange(departmentCode, start, end).stream()
                .map(run -> OrderRunResponse.from(run, getItems(run))).toList());
    }

    @GetMapping("/runs/{orderRunId}/items")
    public ResponseEntity<List<OrderRunItemResponse>> getItemsForRun(@PathVariable UUID orderRunId) {
        return ResponseEntity.ok(reportService.getItemsForRun(orderRunId).stream()
                .map(OrderRunItemResponse::from).toList());
    }

    private List<OrderRunItemResponse> getItems(OrderRun run) {
        return reportService.getItemsForRun(run.getId()).stream()
                .map(OrderRunItemResponse::from).toList();
    }

}
