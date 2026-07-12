package com.example.demo;

import com.example.demo.entities.CoffeeShop;
import com.example.demo.entities.MenuOption;
import com.example.demo.repository.CoffeeShopRepository;
import com.example.demo.repository.MenuOptionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.List;

@Component
public class DataInit implements CommandLineRunner {
    private final CoffeeShopRepository coffeeShopRepository;
    private final MenuOptionRepository menuOptionRepository;

    public DataInit(CoffeeShopRepository coffeeShopRepository, MenuOptionRepository menuOptionRepository) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.menuOptionRepository = menuOptionRepository;
    }

    @Override
    public void run(String... args) {
        if (coffeeShopRepository.count() > 0) return;

        CoffeeShop remedy = new CoffeeShop();
        remedy.setName("Remedy");
        remedy.setLocation("Central Ave");
        coffeeShopRepository.save(remedy);
        seedMenu(remedy);

        CoffeeShop oldCity = new CoffeeShop();
        oldCity.setName("Old City Java");
        oldCity.setLocation("Downtown");
        coffeeShopRepository.save(oldCity);
        seedMenu(oldCity);
    }

    private void seedMenu(CoffeeShop shop) {
        List<MenuOption> options = List.of(
                option(shop, "Drink", "Latte"),
                option(shop, "Drink", "Macchiato"),
                option(shop, "Drink", "Drip"),
                option(shop, "Temperature", "Hot"),
                option(shop, "Temperature", "Iced"),
                option(shop, "Size", "Small"),
                option(shop, "Size", "Medium"),
                option(shop, "Size", "Large"),
                option(shop, "Milk", "Whole"),
                option(shop, "Milk", "Oat"),
                option(shop, "Milk", "Almond"),
                option(shop, "Milk", "Coconut"),
                option(shop, "Milk", "Skim"),
                option(shop, "Syrup", "Vanilla"),
                option(shop, "Syrup", "Caramel"),
                option(shop, "Syrup", "Lavender"),
                option(shop, "Syrup", "Hazelnut")
                );
        menuOptionRepository.saveAll(options);
    }

    private MenuOption option(CoffeeShop shop, String category, String name) {
        MenuOption opt = new MenuOption();
        opt.setCoffeeShop(shop);
        opt.setCategory(category);
        opt.setName(name);
        opt.setIs_available(true);
        return opt;
    }

}
