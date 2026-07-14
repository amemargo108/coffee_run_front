package com.example.demo.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_selections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // using @ManyToOne lets Hibernate manage the foreign key relationship
    // to look up the ID you call orderSelection.getPreferredOrder()
    @ManyToOne
    @JoinColumn(name = "preferred_order_id")
    private PreferredOrder preferredOrder;

    @ManyToOne
    @JoinColumn(name = "menu_option_id")
    private MenuOption menuOption;

}
