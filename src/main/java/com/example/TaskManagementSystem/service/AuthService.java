package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.repository.UserRepository;
import com.example.TaskManagementSystem.security.SecurityService;
import com.example.TaskManagementSystem.web.model.CreateUserRequest;
import com.example.TaskManagementSystem.web.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final SecurityService securityService;

    public ResponseEntity registerUser(@RequestBody CreateUserRequest createUserRequest) {
        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageFormat.format("Email {0} is already in use!", createUserRequest.getEmail()));
        }
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageFormat.format("Username {0} is already in use!", createUserRequest.getUsername()));
        }

        securityService.register(createUserRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("User with data: " + createUserRequest + " is created!");
    }

    public ResponseEntity authUser(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(securityService.authenticateUser(loginRequest));
    }
}
