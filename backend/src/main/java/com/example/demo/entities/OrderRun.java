package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_runs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRun {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "coffee_shop_id")
    private CoffeeShop coffeeShop;

    private String departmentCode;
    private LocalDateTime pulledAt;

    @ManyToOne
    @JoinColumn(name = "runner_id")
    private Employee runner;
}
