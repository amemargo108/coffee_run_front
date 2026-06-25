package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_run_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRunItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_run_id")
    private OrderRun orderRun;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private String orderSummary;
}
