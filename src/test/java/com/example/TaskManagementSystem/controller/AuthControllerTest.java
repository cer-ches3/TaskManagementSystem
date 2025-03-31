package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.service.AuthService;
import com.example.TaskManagementSystem.web.model.CreateUserRequest;
import com.example.TaskManagementSystem.web.model.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test registerUser if request is valid")
    public void testRegisterUser_ifRequestIsValid() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        ResponseEntity expectedResponse = ResponseEntity.ok("User created!");
        when(authService.registerUser(createUserRequest)).thenReturn(expectedResponse);

        ResponseEntity actualResponse = authController.registerUser(createUserRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(authService, times(1)).registerUser(createUserRequest);
    }

    @Test
    @DisplayName("Test registerUser if request is null")
    public void testRegisterUser_IfRequestIsNull() {
        when(authService.registerUser(null)).thenThrow(new IllegalArgumentException("CreateUserRequest cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> authController.registerUser(null));
        verify(authService, times(1)).registerUser(null);
    }

    @Test
    @DisplayName("Test authUser if request is valid")
    public void testAuthUser_ifRequestIsValid() {
        LoginRequest loginRequest = new LoginRequest();
        ResponseEntity expectedResponse = ResponseEntity.ok("User created!");
        when(authService.authUser(loginRequest)).thenReturn(expectedResponse);

        ResponseEntity actualResponse = authController.authUser(loginRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(authService, times(1)).authUser(loginRequest);
    }

    @Test
    @DisplayName("Test authUser if request is null")
    public void testAuthUser_ifRequestIsNull() {
        when(authService.authUser(null)).thenThrow(new IllegalArgumentException("LoginRequest cannot be null"));

        assertThrows(IllegalArgumentException.class, () -> authController.authUser(null));
        verify(authService, times(1)).authUser(null);
    }
}
