package com.example.demo.services;

import com.example.demo.entities.CoffeeShop;
import com.example.demo.entities.MenuOption;
import com.example.demo.repository.CoffeeShopRepository;
import com.example.demo.repository.MenuOptionRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CoffeeShopService {
    private final CoffeeShopRepository coffeeShopRepository;
    private final MenuOptionRepository menuOptionRepository;

    public CoffeeShopService(CoffeeShopRepository coffeeShopRepository, MenuOptionRepository menuOptionRepository) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.menuOptionRepository = menuOptionRepository;
    }

    public List<CoffeeShop> getAll() {
        return coffeeShopRepository.findAll();
    }

    public List<MenuOption> getMenuOptions(UUID coffeeShopId) {
        return menuOptionRepository.findByCoffeeShopId(coffeeShopId);
    }

    public List<MenuOption> getMenuOptionsByCategory(UUID coffeeShopId, String category) {
        return menuOptionRepository.findByCoffeeShopIdAndCategory(coffeeShopId, category);
    }
    //this is so admins can create coffeeshops
    @PreAuthorize("hasRole('ADMIN')")
    public CoffeeShop create(CoffeeShop coffeeShop) {
        return coffeeShopRepository.save(coffeeShop);
    }
    //this is so admins can update coffeeshops
    @PreAuthorize("hasRole('ADMIN')")
    public CoffeeShop update(UUID id, CoffeeShop updated) {
        CoffeeShop existing = coffeeShopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This coffee shoop could not be found: " + id));
        existing.setName(updated.getName());
        existing.setLocation(updated.getLocation());
        return coffeeShopRepository.save(existing);
    }
    // so admins can delete coffeeshops
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(UUID id) {
        coffeeShopRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MenuOption addMenuOption(MenuOption menuOption) {
        return menuOptionRepository.save(menuOption);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public MenuOption updateMenuOption(UUID id, MenuOption updated) {
        MenuOption existing = menuOptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("This menu option could not found: " + id));
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setIs_available(updated.getIs_available());
        return menuOptionRepository.save(existing);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteMenuOption(UUID id) {
        menuOptionRepository.deleteById(id);
    }
}
