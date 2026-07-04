package com.example.demo.dtos;

import com.example.demo.entities.Employee;
import lombok.Data;

import java.util.UUID;

@Data
public class EmployeeResponse {
    private UUID id;
    private String name;
    private String email;
    private String departmentCode;
    private Boolean isAdmin;

    public static EmployeeResponse from(Employee employee) {
        EmployeeResponse response = new EmployeeResponse();
        response.setId(employee.getId());
        response.setName(employee.getName());
        response.setEmail(employee.getEmail());
        response.setDepartmentCode(employee.getDepartment().getCode());
        response.setIsAdmin(employee.getIs_admin());
        return response;
    }
}
