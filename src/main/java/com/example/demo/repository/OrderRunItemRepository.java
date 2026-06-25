package com.example.demo.repository;

import com.example.demo.entities.OrderRunItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRunItemRepository extends JpaRepository<OrderRunItem, UUID> {
    List<OrderRunItem> findByOrderRunItem(UUID orderRunItem);
}
