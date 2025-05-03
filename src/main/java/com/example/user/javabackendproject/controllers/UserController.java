//package com.example.user.javabackendproject.controllers;
//
//import com.example.user.javabackendproject.ApiResponse;
//import com.example.user.javabackendproject.dto.UserDto;
//import com.example.user.javabackendproject.services.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    @Autowired
//    private final UserService userService;
//
//    @GetMapping("/all")
//    public ApiResponse<List<UserDto>> getAllUsers() {
//        List<UserDto> users = userService.getAllUsers();
//        return new ApiResponse<>(true, "Users fetched successfully", users);
//    }
//
////    @PostMapping
////    @ResponseStatus(HttpStatus.CREATED)
////    public ApiResponse<UserDto> createUser(@RequestBody UserDto userDto) {
////        UserDto createdUser = userService.addUser(userDto);
////        return new ApiResponse<>(true, "User created successfully", createdUser);
////    }
//
//    @GetMapping("/{id}")
//    public ApiResponse<UserDto> getUserById(@PathVariable Long id) {
//        Optional<UserDto> userDto = userService.getUserById(id);
//        if (userDto.isPresent()) {
//            return new ApiResponse<>(true, "User fetched successfully", userDto.get());
//        } else {
//            return new ApiResponse<>(false, "User not found");
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ApiResponse<Void> deleteUserById(@PathVariable Long id) {
//        userService.deleteUserById(id);
//        return new ApiResponse<>(true, "User deleted successfully");
//    }
//}

package com.example.user.javabackendproject.controllers;

import com.example.user.javabackendproject.ApiResponse;
import com.example.user.javabackendproject.dto.UserDto;
import com.example.user.javabackendproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        if(users!=null)
        {
            return new ResponseEntity<>(new ApiResponse<>(true, "Users fetched successfully", users),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ApiResponse<>(false, "Unable to fetch users", users),HttpStatus.BAD_REQUEST);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        Optional<UserDto> userDto = userService.getUserById(id);
        if (userDto.isPresent()) {
            return new ResponseEntity<>(new ApiResponse<>(true, "User fetched successfully", userDto.get()),HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ApiResponse<>(false, "User not found"),HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return new ApiResponse<>(true, "User deleted successfully");
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse<Void>> addOrderToUser(@PathVariable Long userId, @RequestBody String orderId) {
        try {
            userService.addOrderToUser(userId, orderId);
            return new ResponseEntity<>(new ApiResponse<>(true, "Order added successfully"), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, "Unable to add the order"), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse<List<String>>> getUserOrders(@PathVariable Long userId) {
        try {
            List<String> orders = userService.getUserOrders(userId);
            return new ResponseEntity<>(new ApiResponse<>(true, "Orders fetched successfully", orders), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse<>(false, "No orders found"), HttpStatus.NOT_FOUND);
        }
    }


}
