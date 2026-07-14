package com.example.demo.dtos;

import com.example.demo.entities.OrderRunItem;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderRunItemResponse {
    private UUID id;
    private String employeeName;
    private String orderSummary;

    public static OrderRunItemResponse from(OrderRunItem item) {
        OrderRunItemResponse response = new OrderRunItemResponse();
        response.setId(item.getId());
        response.setEmployeeName(item.getEmployee().getName());
        response.setOrderSummary(item.getOrderSummary());
        return response;
    }
}
