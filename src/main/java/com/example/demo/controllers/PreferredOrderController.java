package com.example.demo.controllers;

import com.example.demo.entities.PreferredOrder;
import com.example.demo.services.PreferredOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/preferred-orders")
public class PreferredOrderController {
    private final PreferredOrderService preferredOrderService;

    public PreferredOrderController(PreferredOrderService preferredOrderService) {
        this.preferredOrderService = preferredOrderService;
    }

    @GetMapping("/department/{departmentCode}/shop/{coffeeShopId}")
    public ResponseEntity<List<PreferredOrder>> getOrdersForDepartmentAndShop(@PathVariable String departmentCode, @PathVariable UUID coffeeShopId) {
        return ResponseEntity.ok(preferredOrderService.getOrdersForDepartmentAndShop(departmentCode, coffeeShopId));
    }

    @PostMapping("/employee/{employeeId}/shop/{coffeeShopId}")
    public ResponseEntity<PreferredOrder> saveOrUpdate(@PathVariable UUID employeeId, @PathVariable UUID coffeeShopId, @RequestBody List<UUID> menuOptionIds) {
        return ResponseEntity.ok(preferredOrderService.saveOrUpdate(employeeId, coffeeShopId, menuOptionIds));
    }

    @DeleteMapping("/{orderId}/employee/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable UUID orderId, @PathVariable UUID employeeId) {
        preferredOrderService.delete(orderId, employeeId);
        return ResponseEntity.noContent().build();
    }
}
