package com.example.demo.repository;

import com.example.demo.entities.MenuOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MenuOptionRepository extends JpaRepository<MenuOption, UUID> {
    List<MenuOption> findByCoffeeShopId(UUID coffeeShopId);
    List<MenuOption> findByCoffeeShopIdAndCategory(UUID coffeeShopId, String category);
}
