package com.example.user.javabackendproject.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserDto {

    private Long id;
    private String email;
    private String username;
    private ArrayList<String> orderList;
}
