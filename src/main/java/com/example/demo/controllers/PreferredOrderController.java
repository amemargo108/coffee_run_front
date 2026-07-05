package com.example.demo.controllers;

import com.example.demo.dtos.PreferredOrderResponse;
import com.example.demo.entities.PreferredOrder;
import com.example.demo.repository.MenuOptionRepository;
import com.example.demo.repository.OrderSelectionRepository;
import com.example.demo.services.PreferredOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/preferred-orders")
public class PreferredOrderController {
    private final PreferredOrderService preferredOrderService;
    private final OrderSelectionRepository orderSelectionRepository;
    private final MenuOptionRepository menuOptionRepository;

    public PreferredOrderController(PreferredOrderService preferredOrderService, OrderSelectionRepository orderSelectionRepository, MenuOptionRepository menuOptionRepository) {
        this.preferredOrderService = preferredOrderService;
        this.menuOptionRepository = menuOptionRepository;
        this.orderSelectionRepository = orderSelectionRepository;
    }

    @GetMapping("/department/{departmentCode}/shop/{coffeeShopId}")
    public ResponseEntity<List<PreferredOrderResponse>> getOrdersForDepartmentAndShop(@PathVariable String departmentCode, @PathVariable UUID coffeeShopId) {
        List<PreferredOrderResponse> response = preferredOrderService.getOrdersForDepartmentAndShop(departmentCode, coffeeShopId).stream().map(order -> {
                    List<String> selections = orderSelectionRepository
                            .findByPreferredOrderId(order.getId())
                            .stream().map(
                                    s -> s.getMenuOption().getName()).toList();
                    return PreferredOrderResponse.from(order, selections);
                })
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/employee/{employeeId}/shop/{coffeeShopId}")
    public ResponseEntity<PreferredOrderResponse> saveOrUpdate(@PathVariable UUID employeeId, @PathVariable UUID coffeeShopId, @RequestBody List<UUID> menuOptionIds) {
        PreferredOrder saved = preferredOrderService.saveOrUpdate(employeeId, coffeeShopId, menuOptionIds);
        List<String> selections = menuOptionIds.stream().map(id -> menuOptionRepository.findById(id)
                .orElseThrow().getName()).toList();
        return ResponseEntity.ok(PreferredOrderResponse.from(saved, selections));
    }



    @DeleteMapping("/{orderId}/employee/{employeeId}")
    public ResponseEntity<Void> delete(@PathVariable UUID orderId, @PathVariable UUID employeeId) {
        preferredOrderService.delete(orderId, employeeId);
        return ResponseEntity.noContent().build();
    }
}
