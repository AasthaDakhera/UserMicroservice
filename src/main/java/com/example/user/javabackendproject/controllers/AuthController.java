package com.example.user.javabackendproject.controllers;

import com.example.user.javabackendproject.ApiResponse;
import com.example.user.javabackendproject.dto.LoginDto;
import com.example.user.javabackendproject.dto.SignUpDto;
import com.example.user.javabackendproject.dto.UserDetailsDto;
import com.example.user.javabackendproject.dto.UserDto;
import com.example.user.javabackendproject.services.AuthService;
import com.example.user.javabackendproject.services.JwtService;
import com.example.user.javabackendproject.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final JwtService jwtService;


    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecretKey.getBytes(StandardCharsets.UTF_8));
    }


    //    @CrossOrigin
//    @PostMapping("/signup")
//    public ResponseEntity<UserDto> signUp(@RequestBody SignUpDto signUpDto) {
//        UserDto userDto = userService.signUp(signUpDto);
//        return ResponseEntity.ok(userDto);
//    }
////
@CrossOrigin
@PostMapping("/signup")
public ResponseEntity<ApiResponse<String>> signUp(@RequestBody SignUpDto signUpDto) {
    try {
        UserDto userDto = userService.signUp(signUpDto);
        String username = userDto.getUsername();

        ApiResponse<String> apiResponse = new ApiResponse<>(true, "Signup successful", username);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    } catch (Exception e) {
        ApiResponse<String> apiResponse = new ApiResponse<>(false, "Signup failed: " + e.getMessage(), null);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}



    @CrossOrigin
    @GetMapping("/username")
    public ResponseEntity<ApiResponse<UserDetailsDto>> getusername(HttpServletRequest request){
        try
        {
//            Claims claims = Jwts.parser()
//                    .setSigningKey(getSecretKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();

//            String username = claims.get("username", String.class);
            String username = request.getHeader("username");
            String useremail = request.getHeader("useremail");

            UserDetailsDto userDetailsDto = new UserDetailsDto();

            userDetailsDto.setUsername(username); // Use setter methods instead of direct field access
            userDetailsDto.setUseremail(useremail);


            ApiResponse<UserDetailsDto> apiResponse = new ApiResponse<>(true, "username fetched successfully", userDetailsDto);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {
            ApiResponse<UserDetailsDto> apiResponse = new ApiResponse<>(false, "unable to fetch the username");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
                                                     HttpServletResponse response) {
        try {
            String token = authService.login(loginDto);
            Cookie cookie = new Cookie("AuthToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);

            ApiResponse<String> apiResponse = new ApiResponse<>(true, "Login successful", token);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(false, "Login failed: " + e.getMessage(), null);
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}
