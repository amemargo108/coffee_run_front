package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employees")
// JPA @Data automatically generates getters/setters/equals/hashCode/and toString for each field
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Employee implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "department_code")
    private Department department;

    private Boolean is_admin;

    private String name;

    private String email;

    private String password;

    //this is so Spring Security knows what role this user has (admin or not)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = is_admin ? "ROLE_ADMIN" : "ROLE_EMPLOYEE";
        return List.of(new SimpleGrantedAuthority(role));
    }
    // this is so Spring Security knows which field is their username
    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
