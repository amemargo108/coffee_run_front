package com.example.demo.dtos;

import lombok.Data;

@Data
public class CreateEmployee {
    private String name;
    private String email;
    private String password;
    private String departmentCode;
    private Boolean isAdmin;
}

