package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.service.AuthService;
import com.example.TaskManagementSystem.web.model.CreateUserRequest;
import com.example.TaskManagementSystem.web.model.LoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "AuthController", description = "Контроллер регистрации и авторизации пользователей.")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Метод регистрации.",
            description = "Метод отвечает за регистрацию нового пользователя.")
    public ResponseEntity registerUser(@RequestBody CreateUserRequest createUserRequest) {
        return authService.registerUser(createUserRequest);
    }

    @PostMapping("/signin")
    @Operation(summary = "Метод авторизации.",
            description = "Метод отвечает за авторизацию пользователя в системе по связке email+password или по jwt-токену.")
    public ResponseEntity authUser(@RequestBody LoginRequest loginRequest) {
        return authService.authUser(loginRequest);
    }
}
