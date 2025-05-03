package com.example.user.javabackendproject.dto;

import com.example.user.javabackendproject.entities.enums.Role;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpDto {
    private String email;
    private String password;
    private String username;
    private Set<Role> roles;
}
