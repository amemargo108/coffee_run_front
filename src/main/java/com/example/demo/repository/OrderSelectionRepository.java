package com.example.demo.repository;

import com.example.demo.entities.OrderSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderSelectionRepository extends JpaRepository<OrderSelection, UUID> {
    List<OrderSelection> findByPreferredOrderId(UUID preferredOrderId);
    void deleteByPreferredOrderId(UUID preferredOrderId);
}
