package com.example.demo.services;


import com.example.demo.entities.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

// this service is for handling login
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(EmployeeRepository employeeRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.employeeRepository = employeeRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public String login(String email, String password) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow();

        return jwtService.generateToken(employee);
    }
}
