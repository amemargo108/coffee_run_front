package com.example.demo.controllers;


import com.example.demo.entities.CoffeeShop;
import com.example.demo.entities.MenuOption;
import com.example.demo.services.CoffeeShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/coffee-shops")
public class CoffeeShopController {
    private final CoffeeShopService coffeeShopService;

    public CoffeeShopController(CoffeeShopService coffeeShopService) {
        this.coffeeShopService = coffeeShopService;
    }

    @GetMapping
    public ResponseEntity<List<CoffeeShop>> getAll() {
        return ResponseEntity.ok(coffeeShopService.getAll());
    }

    @GetMapping("/{id}/menu")
    public ResponseEntity<List<MenuOption>> getMenuOptions(@PathVariable UUID id) {
        return ResponseEntity.ok(coffeeShopService.getMenuOptions(id));
    }

    @GetMapping("/{id}/menu/{category}")
    public ResponseEntity<List<MenuOption>> getMenuOptionsByCategory(@PathVariable UUID id, @PathVariable String category) {
        return ResponseEntity.ok(coffeeShopService.getMenuOptionsByCategory(id, category));
    }

    @PostMapping
    public ResponseEntity<CoffeeShop> create(@RequestBody CoffeeShop coffeeShop) {
        return ResponseEntity.ok(coffeeShopService.create(coffeeShop));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoffeeShop> update(@PathVariable UUID id, @RequestBody CoffeeShop coffeeShop) {
        return ResponseEntity.ok(coffeeShopService.update(id, coffeeShop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        coffeeShopService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<MenuOption> addMenuOption(@PathVariable UUID id, @RequestBody MenuOption menuOption) {
        menuOption.setCoffeeShop(coffeeShopService.getAll()
                .stream()
                .filter(cs -> cs.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("This coffee shop could not be found: " + id)));
        return ResponseEntity.ok(coffeeShopService.addMenuOption(menuOption));
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<MenuOption> updateMenuOption(@PathVariable UUID id, @RequestBody MenuOption menuOption) {
        return ResponseEntity.ok(coffeeShopService.updateMenuOption(id, menuOption));
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<Void> deleteMenuOption(@PathVariable UUID id) {
        coffeeShopService.deleteMenuOption(id);
        return ResponseEntity.noContent().build();
    }
}
