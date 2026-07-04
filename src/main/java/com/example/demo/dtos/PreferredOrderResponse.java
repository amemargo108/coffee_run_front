package com.example.demo.dtos;

import com.example.demo.entities.PreferredOrder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PreferredOrderResponse {
    private UUID id;
    private String employeeName;
    private String coffeeShopName;
    private List<String> selections;

    public static PreferredOrderResponse from(PreferredOrder order, List<String> selectionNames) {
        PreferredOrderResponse response = new PreferredOrderResponse();
        response.setId(order.getId());
        response.setEmployeeName(order.getEmployee().getName());
        response.setCoffeeShopName(order.getCoffeeShop().getName());
        response.setSelections(selectionNames);
        return response;
    }
}
