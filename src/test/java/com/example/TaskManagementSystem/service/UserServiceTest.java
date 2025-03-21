package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getAll")
    public void testGetAll() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "test1", "test1@example.com", "password1", new HashSet<>()));
        users.add(new User(2L, "test2", "test2@example.com", "password2", new HashSet<>()));
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();

        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getUsername());
        assertEquals("test1@example.com", result.get(0).getEmail());
        assertEquals("password1", result.get(0).getPassword());
        assertEquals("test2", result.get(1).getUsername());
        assertEquals("test2@example.com", result.get(1).getEmail());
        assertEquals("password2", result.get(1).getPassword());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getUserById if user is found")
    public void testGetUserById_ifUserIsFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        ResponseEntity result = userService.getById(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test getUserById if user not found")
    public void testGetUserById_ifUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity result = userService.getById(userId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Test deleteById if user is found")
    public void testDeleteById_ifUserIsFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        ResponseEntity result = userService.deleteById(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    @DisplayName("Test deleteById if user not found")
    public void testDeleteById_ifUserNotFound() {
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        ResponseEntity result = userService.deleteById(userId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(userRepository, times(1)).existsById(userId);
    }

    @Test
    @DisplayName("Test update if user is found")
    public void testUpdate_ifUserIsFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));

        ResponseEntity result = userService.update(userId, new User());

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(new User());
    }

    @Test
    @DisplayName("Test update if user not found")
    public void testUpdate_ifUserNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity result = userService.update(userId, new User());

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(0)).save(new User());
    }
}
