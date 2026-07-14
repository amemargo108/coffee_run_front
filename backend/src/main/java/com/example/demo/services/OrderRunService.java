package com.example.demo.services;

import com.example.demo.entities.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderRunService {

    private final OrderRunRepository orderRunRepository;
    private final OrderRunItemRepository orderRunItemRepository;
    private final PreferredOrderRepository preferredOrderRepository;
    private final OrderSelectionRepository orderSelectionRepository;
    private final EmployeeRepository employeeRepository;
    private final CoffeeShopRepository coffeeShopRepository;

    public OrderRunService(
            OrderRunRepository orderRunRepository,
            OrderRunItemRepository orderRunItemRepository,
            PreferredOrderRepository preferredOrderRepository,
            OrderSelectionRepository orderSelectionRepository,
            EmployeeRepository employeeRepository,
            CoffeeShopRepository coffeeShopRepository) {
        this.orderRunRepository = orderRunRepository;
        this.orderRunItemRepository = orderRunItemRepository;
        this.preferredOrderRepository = preferredOrderRepository;
        this.orderSelectionRepository = orderSelectionRepository;
        this.employeeRepository = employeeRepository;
        this.coffeeShopRepository = coffeeShopRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public OrderRun pullOrderList(UUID runnerId, UUID coffeeShopId, String departmentCode) {
        Employee runner = employeeRepository.findById(runnerId)
                .orElseThrow(() -> new RuntimeException("This employee could not be found: " + runnerId));
        CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId)
                .orElseThrow(() -> new RuntimeException("This coffee shop could not be found: " + coffeeShopId));

        OrderRun orderRun = new OrderRun();
        orderRun.setRunner(runner);
        orderRun.setCoffeeShop(coffeeShop);
        orderRun.setDepartmentCode(departmentCode);
        orderRun.setPulledAt(LocalDateTime.now());
        OrderRun savedRun = orderRunRepository.save(orderRun);

        List<PreferredOrder> orders = preferredOrderRepository
                .findByEmployeeDepartmentCodeAndCoffeeShopId(departmentCode, coffeeShopId);

        List<OrderRunItem> items = orders.stream().map(order -> {
            List<OrderSelection> selections = orderSelectionRepository
                    .findByPreferredOrderId(order.getId());
            String summary = selections.stream()
                    .map(s -> s.getMenuOption().getName())
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("No selections");
            OrderRunItem item = new OrderRunItem();
            item.setOrderRun(savedRun);
            item.setEmployee(order.getEmployee());
            item.setOrderSummary(summary);
            return item;
        }).toList();

        orderRunItemRepository.saveAll(items);
        return savedRun;
    }

    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public OrderRun getRunById(UUID id) {
        return orderRunRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This order run could not be found: " + id));
    }

    public List<OrderRunItem> getItemsForRun(UUID orderRunId) {
        return orderRunItemRepository.findByOrderRunId(orderRunId);
    }
}