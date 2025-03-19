package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.service.AuthService;
import com.example.TaskManagementSystem.web.model.CreateUserRequest;
import com.example.TaskManagementSystem.web.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody CreateUserRequest createUserRequest) {
        return authService.registerUser(createUserRequest);
    }

    @PostMapping("/signin")
    public ResponseEntity authUser(@RequestBody LoginRequest loginRequest) {
        return authService.authUser(loginRequest);
    }
}
