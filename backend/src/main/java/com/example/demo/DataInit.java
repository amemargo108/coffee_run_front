package com.example.demo;

import com.example.demo.entities.CoffeeShop;
import com.example.demo.entities.Department;
import com.example.demo.entities.Employee;
import com.example.demo.entities.MenuOption;
import com.example.demo.repository.CoffeeShopRepository;
import com.example.demo.repository.DepartmentRepository;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.MenuOptionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInit implements CommandLineRunner {
    private final CoffeeShopRepository coffeeShopRepository;
    private final MenuOptionRepository menuOptionRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInit(CoffeeShopRepository coffeeShopRepository, MenuOptionRepository menuOptionRepository, DepartmentRepository departmentRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.coffeeShopRepository = coffeeShopRepository;
        this.menuOptionRepository = menuOptionRepository;
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${seed.employee.password}")
    private String seedEmployeePassword;

    @Override
    public void run(String... args) {
        if (coffeeShopRepository.count() == 0) {
            //add coffeshops
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
        if (departmentRepository.count() == 0) {
            // add departments
            Department eng = new Department();
            eng.setCode("ENG");
            eng.setName("Engineering");
            departmentRepository.save(eng);

            Department acc = new Department();
            acc.setName("Accounting");
            acc.setCode("ACC");
            departmentRepository.save(acc);
        }
        if (employeeRepository.count() == 0) {
            Department eng = departmentRepository.findByCode("ENG").orElse(null);
            Employee runner = new Employee();
            runner.setName("Fake Runner");
            runner.setEmail("runner@coffeerun.com");
            runner.setPassword(passwordEncoder.encode("runnerpassword"));
            runner.setIs_admin(false);
            runner.setDepartment(eng);
            employeeRepository.save(runner);

            Employee schoolAdmin = new Employee();
            schoolAdmin.setName("Fake Admin");
            schoolAdmin.setEmail("schooladmin@coffeerun.com");
            schoolAdmin.setPassword(passwordEncoder.encode(seedEmployeePassword));
            schoolAdmin.setIs_admin(true);
            schoolAdmin.setDepartment(eng);
            employeeRepository.save(schoolAdmin);
        }
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
