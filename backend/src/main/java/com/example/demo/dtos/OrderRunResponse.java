package com.example.demo.dtos;

import com.example.demo.entities.OrderRun;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderRunResponse {
    private UUID id;
    private String runnerName;
    private String coffeeShopName;
    private String departmentCode;
    private LocalDateTime pulledAt;
    private List<OrderRunItemResponse> items;

    public static OrderRunResponse from(OrderRun run, List<OrderRunItemResponse> items) {
        OrderRunResponse response = new OrderRunResponse();
        response.setId(run.getId());
        response.setRunnerName(run.getRunner().getName());
        response.setCoffeeShopName(run.getCoffeeShop().getName());
        response.setDepartmentCode(run.getDepartmentCode());
        response.setPulledAt(run.getPulledAt());
        response.setItems(items);
        return response;
    }
}
