package com.example.demo.services;

import com.example.demo.entities.CoffeeShop;
import com.example.demo.entities.Employee;
import com.example.demo.entities.MenuOption;
import com.example.demo.entities.PreferredOrder;
import com.example.demo.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PreferredOrderServiceTest {

    @Mock
    private PreferredOrderRepository preferredOrderRepository;

    @Mock
    private OrderSelectionRepository orderSelectionRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CoffeeShopRepository coffeeShopRepository;

    @Mock
    private MenuOptionRepository menuOptionRepository;

    @InjectMocks
    private PreferredOrderService preferredOrderService;

    // test for creating a new order when none exists
    @Test
    void saveOrUpdate_createsNewOrder_whenNoneExists() {
        UUID employeeId = UUID.randomUUID();
        UUID coffeeShopId = UUID.randomUUID();
        UUID menuOptionId = UUID.randomUUID();

        CoffeeShop coffeeShop = new CoffeeShop();
        coffeeShop.setId(coffeeShopId);

        Employee employee = new Employee();
        employee.setId(employeeId);

        MenuOption menuOption = new MenuOption();
        menuOption.setId(menuOptionId);

        PreferredOrder savedOrder = new PreferredOrder();
        savedOrder.setId(UUID.randomUUID());
        savedOrder.setEmployee(employee);

        when(preferredOrderRepository.findByEmployeeIdAndCoffeeShopId(employeeId, coffeeShopId)).thenReturn(Optional.empty());

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        when(preferredOrderRepository.save(any(PreferredOrder.class))).thenReturn(savedOrder);

        when(menuOptionRepository.findById(menuOptionId)).thenReturn(Optional.of(menuOption));

        when(coffeeShopRepository.findById(coffeeShopId)).thenReturn(Optional.of(coffeeShop));

        PreferredOrder result = preferredOrderService.saveOrUpdate(employeeId, coffeeShopId, List.of(menuOptionId));

        assertNotNull(result);
        verify(preferredOrderRepository, times(1)).save(any(PreferredOrder.class));
        verify(orderSelectionRepository, times(1)).saveAll(any());
    }

    // updating an existing order instead of creating a duplicate
    @Test
    void saveOrUpdate_updatesExistingOrder_whenOneExists() {
        UUID employeeId = UUID.randomUUID();
        UUID coffeeShopId = UUID.randomUUID();
        UUID menuOptionId = UUID.randomUUID();

        Employee employee = new Employee();
        employee.setId(employeeId);

        PreferredOrder existingOrder = new PreferredOrder();
        existingOrder.setId(UUID.randomUUID());
        existingOrder.setEmployee(employee);

        MenuOption menuOption = new MenuOption();
        menuOption.setId(menuOptionId);

        when(preferredOrderRepository.findByEmployeeIdAndCoffeeShopId(employeeId, coffeeShopId)).thenReturn(Optional.of(existingOrder));
        when(preferredOrderRepository.save(any(PreferredOrder.class))).thenReturn(existingOrder);
        when(menuOptionRepository.findById(menuOptionId)).thenReturn(Optional.of(menuOption));

        PreferredOrder result = preferredOrderService.saveOrUpdate(employeeId, coffeeShopId, List.of(menuOptionId));

        assertNotNull(result);
        verify(employeeRepository, never()).findById(any());
        verify(orderSelectionRepository, times(1)).deleteByPreferredOrderId(existingOrder.getId());
        verify(orderSelectionRepository, times(1)).saveAll(any());
    }
}
