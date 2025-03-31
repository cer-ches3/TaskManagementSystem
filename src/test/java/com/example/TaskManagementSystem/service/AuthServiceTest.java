package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.repository.UserRepository;
import com.example.TaskManagementSystem.security.SecurityService;
import com.example.TaskManagementSystem.web.model.AuthResponse;
import com.example.TaskManagementSystem.web.model.CreateUserRequest;
import com.example.TaskManagementSystem.web.model.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private AuthService authService;

    private CreateUserRequest createUserRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("username");
        createUserRequest.setEmail("email@mail.ru");
        createUserRequest.setPassword("password1");
        createUserRequest.setRoles(new HashSet<>());
    }

    @Test
    @DisplayName("Test registerUser if request is valid")
    public void testRegisterUser_ifRequestIsValid() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(createUserRequest.getUsername())).thenReturn(false);

        ResponseEntity result = authService.registerUser(createUserRequest);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("User with data: " + createUserRequest + " is created!", result.getBody());
        verify(userRepository, times(1)).existsByUsername(createUserRequest.getUsername());
        verify(userRepository, times(1)).existsByEmail(createUserRequest.getEmail());
    }

    @Test
    @DisplayName("Test registerUser if email from request is found")
    public void testRegisterUser_ifEmailFromRequestIsFound() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(true);
        when(userRepository.existsByUsername(createUserRequest.getUsername())).thenReturn(false);

        ResponseEntity result = authService.registerUser(createUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(MessageFormat.format("Email {0} is already in use!", createUserRequest.getEmail()), result.getBody());
        verify(userRepository, times(1)).existsByEmail(createUserRequest.getEmail());
        verify(userRepository, times(0)).existsByUsername(createUserRequest.getUsername());
    }

    @Test
    @DisplayName("Test registerUser if username from request is found")
    public void testRegisterUser_ifUsernameFromRequestIsFound() {
        when(userRepository.existsByEmail(createUserRequest.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(createUserRequest.getUsername())).thenReturn(true);

        ResponseEntity result = authService.registerUser(createUserRequest);

        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertEquals(MessageFormat.format("Username {0} is already in use!", createUserRequest.getUsername()), result.getBody());
        verify(userRepository, times(1)).existsByEmail(createUserRequest.getEmail());
        verify(userRepository, times(1)).existsByUsername(createUserRequest.getUsername());
    }

    @Test
    @DisplayName("Test authUser")
    public void testAuthUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("email@mail.ru");
        loginRequest.setPassword("password");

        AuthResponse authResponse = new AuthResponse(1L, "test-token", "username", "email@mail.ru", new ArrayList<>());
        when(securityService.authenticateUser(loginRequest)).thenReturn(authResponse);

        ResponseEntity result = authService.authUser(loginRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(securityService, times(1)).authenticateUser(loginRequest);
    }
}
