package com.example.demo.services;

import com.example.demo.dtos.PreferredOrderResponse;
import com.example.demo.entities.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PreferredOrderService {
    private final PreferredOrderRepository preferredOrderRepository;
    private final OrderSelectionRepository orderSelectionRepository;
    private final EmployeeRepository employeeRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final CoffeeShopRepository coffeeShopRepository;

    public PreferredOrderService(PreferredOrderRepository preferredOrderRepository, OrderSelectionRepository orderSelectionRepository, EmployeeRepository employeeRepository, MenuOptionRepository menuOptionRepository, CoffeeShopRepository coffeeShopRepository) {
        this.preferredOrderRepository = preferredOrderRepository;
        this.orderSelectionRepository = orderSelectionRepository;
        this.employeeRepository = employeeRepository;
        this.menuOptionRepository = menuOptionRepository;
        this.coffeeShopRepository = coffeeShopRepository;
    }

    //this will create the Runner View
    public List<PreferredOrder> getOrdersForDepartmentAndShop(String departmentCode, UUID coffeeShopId) {
        return preferredOrderRepository.findByEmployeeDepartmentCodeAndCoffeeShopId(departmentCode, coffeeShopId);
    }

    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public PreferredOrder saveOrUpdate(UUID employeeId, UUID coffeeShopId, List<UUID> menuOptionIds) {
        PreferredOrder order = preferredOrderRepository.findByEmployeeIdAndCoffeeShopId(employeeId, coffeeShopId)
                .orElseGet(() -> {
                    Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new RuntimeException("This employee could not be found " + employeeId));
                    CoffeeShop coffeeShop = coffeeShopRepository.findById(coffeeShopId).orElseThrow(() -> new RuntimeException("This coffee shop could not be found: " + coffeeShopId));
                    PreferredOrder newOrder = new PreferredOrder();
                    newOrder.setEmployee(employee);
                    newOrder.setCoffeeShop(coffeeShop);
                    return newOrder;
                });

        PreferredOrder saved = preferredOrderRepository.save(order);

        orderSelectionRepository.deleteByPreferredOrderId(saved.getId());

        List<OrderSelection> selections = menuOptionIds.stream().map(optionId -> {
            MenuOption option = menuOptionRepository.findById(optionId).orElseThrow(() -> new RuntimeException("This menu option could not be found " + optionId));
            OrderSelection selection = new OrderSelection();
            selection.setPreferredOrder(saved);
            selection.setMenuOption(option);
            return selection;
        }).toList();

        orderSelectionRepository.saveAll(selections);
        return saved;
    }

    @Transactional
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public void delete(UUID orderId, UUID requestingEmployeeId) {
        PreferredOrder order = preferredOrderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("This order could not be found " + orderId));

        if (!order.getEmployee().getId().equals(requestingEmployeeId)) {
            throw new AccessDeniedException("You can only delete your own orders.");
        }

        orderSelectionRepository.deleteByPreferredOrderId(orderId);
        preferredOrderRepository.delete(order);
    }

    public Optional<PreferredOrderResponse> getByEmployeeAndShop(UUID employeeId, UUID coffeeShopId) {
        return preferredOrderRepository.findByEmployeeIdAndCoffeeShopId(employeeId, coffeeShopId)
                .map(order -> {
                    List<String> selectionNames = orderSelectionRepository.findByPreferredOrderId(order.getId())
                            .stream().map(s -> s.getMenuOption().getName()).toList();
                    return PreferredOrderResponse.from(order, selectionNames);
                });
    }
}
