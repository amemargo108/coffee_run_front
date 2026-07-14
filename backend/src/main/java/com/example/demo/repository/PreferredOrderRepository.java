package com.example.demo.repository;

import com.example.demo.entities.PreferredOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PreferredOrderRepository extends JpaRepository<PreferredOrder, UUID> {
    Optional<PreferredOrder> findByEmployeeIdAndCoffeeShopId(UUID employeeId, UUID coffeeShopId);
    List<PreferredOrder> findByEmployeeDepartmentCode(String departmentCode);
    List<PreferredOrder> findByEmployeeDepartmentCodeAndCoffeeShopId(String departmentCode, UUID coffeeShopId);
}
