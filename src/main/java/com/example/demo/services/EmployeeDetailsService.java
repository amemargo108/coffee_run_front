package com.example.demo.services;

import com.example.demo.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class EmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    public EmployeeDetailsService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("There is no employee registered with the email: " + email));
    }
}
