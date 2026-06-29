package com.example.demo.controllers;

import com.example.demo.entities.OrderRun;
import com.example.demo.entities.OrderRunItem;
import com.example.demo.services.OrderRunService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order-runs")
public class OrderRunController {

    private final OrderRunService orderRunService;

    public OrderRunController(OrderRunService orderRunService) {
        this.orderRunService = orderRunService;
    }

    @PostMapping("/runner/{runnerId}/shop/{coffeeShopId}/department/{departmentCode}")
    public ResponseEntity<OrderRun> pullOrderList(@PathVariable UUID runnerId, @PathVariable UUID coffeeShopId, @PathVariable String departmentCode) {
        return ResponseEntity.ok(orderRunService.pullOrderList(runnerId, coffeeShopId, departmentCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRun> getRunById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderRunService.getRunById(id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderRunItem>> getItemsForRun(@PathVariable UUID id) {
        return ResponseEntity.ok(orderRunService.getItemsForRun(id));
    }

}
