package com.example.user.javabackendproject.services;

import com.example.user.javabackendproject.dto.SignUpDto;
import com.example.user.javabackendproject.dto.UserDto;
import com.example.user.javabackendproject.entities.User;
import com.example.user.javabackendproject.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("User with email "+ username +" not found"));
    }

//    public User getUserById(Long userId) {
//        if (userId == null || userId <= 0) {
//            throw new IllegalArgumentException("Invalid user ID: " + userId);
//        }
//
//        return userRepository.findById(userId)
//                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
//    }



    public UserDto signUp(SignUpDto signUpDto) {
        Optional<User> user = userRepository.findByEmail(signUpDto.getEmail());
        if(user.isPresent()) {
            throw new BadCredentialsException("User with email already exits "+ signUpDto.getEmail());
        }

        User toBeCreatedUser = modelMapper.map(signUpDto, User.class);
        toBeCreatedUser.setPassword(passwordEncoder.encode(toBeCreatedUser.getPassword()));

        User savedUser = userRepository.save(toBeCreatedUser);
        return modelMapper.map(savedUser, UserDto.class);
    }

//
//    public UserDto addUser(UserDto userDto) {
//        User userEntity = new User();
//        BeanUtils.copyProperties(userDto, userEntity);
//
//
//        User savedUser = userRepository.save(userEntity);
//        UserDto responseDto = new UserDto();
//        BeanUtils.copyProperties(savedUser, responseDto);
//        return responseDto;
//    }


    public List<UserDto> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map(user -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto);
            return userDto;
        }).collect(Collectors.toList());
    }


    public Optional<UserDto> getUserById(Long userId) {
        Optional<User> userEntity = userRepository.findById(userId);
        if (userEntity.isPresent()) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity.get(), userDto);
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    public void deleteUserById(Long userId) {

        userRepository.deleteById(userId);
    }

    public void addOrderToUser(Long userId, String orderId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getOrderList() == null) {
            user.setOrderList(new ArrayList<>());
        }
        user.getOrderList().add(orderId);
        userRepository.save(user);
    }

    public List<String> getUserOrders(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user.getOrderList() != null ? user.getOrderList() : new ArrayList<>();
    }
}






















