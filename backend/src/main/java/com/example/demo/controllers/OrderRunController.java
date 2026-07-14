package com.example.demo.controllers;

import com.example.demo.dtos.OrderRunItemResponse;
import com.example.demo.dtos.OrderRunResponse;
import com.example.demo.entities.OrderRun;
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
    public ResponseEntity<OrderRunResponse> pullOrderList(@PathVariable UUID runnerId, @PathVariable UUID coffeeShopId, @PathVariable String departmentCode) {
        OrderRun run = orderRunService.pullOrderList(runnerId, coffeeShopId, departmentCode);
        List<OrderRunItemResponse> items = orderRunService.getItemsForRun(run.getId())
                .stream().map(OrderRunItemResponse::from).toList();
        return ResponseEntity.ok(OrderRunResponse.from(run, items));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderRunResponse> getRunById(@PathVariable UUID id) {
        OrderRun run = orderRunService.getRunById(id);
        List<OrderRunItemResponse> items = orderRunService.getItemsForRun(id).stream()
                .map(OrderRunItemResponse::from).toList();
        return ResponseEntity.ok(OrderRunResponse.from(run, items));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<OrderRunItemResponse>> getItemsForRun(@PathVariable UUID id) {
       return ResponseEntity.ok(orderRunService.getItemsForRun(id).stream()
               .map(OrderRunItemResponse::from).toList());
    }

}
