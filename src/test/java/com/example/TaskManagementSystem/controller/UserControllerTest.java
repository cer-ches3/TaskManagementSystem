package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.RoleType;
import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.service.UserService;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getAllUser")
    public void testGetAllUser() {
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "test1", "test1@example.com", "password1", new HashSet<>()));
        users.add(new User(2L, "test2", "test2@example.com", "password2", new HashSet<>()));
        when(userService.getAll()).thenReturn(users);

        List<User> result = userController.getAllUser();

        assertEquals(2, result.size());
        assertEquals("test1", result.get(0).getUsername());
        assertEquals("test1@example.com", result.get(0).getEmail());
        assertEquals("password1", result.get(0).getPassword());
        assertEquals("test2", result.get(1).getUsername());
        assertEquals("test2@example.com", result.get(1).getEmail());
        assertEquals("password2", result.get(1).getPassword());
        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("Test getUserById if user is found")
    public void testGetUserById_ifUserIsFound() {
        long userId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.OK);
        when(userService.getById(userId)).thenReturn(expectedResponse);

        ResponseEntity result = userController.getUserById(userId);

        assertEquals(expectedResponse, result);
        verify(userService, times(1)).getById(userId);
    }

    @Test
    @DisplayName("Test getUserById if user not found")
    public void testGetUserById_ifUserNotFound() {
        long userId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.NOT_FOUND);
        when(userService.getById(userId)).thenReturn(expectedResponse);

        ResponseEntity result = userController.getUserById(userId);

        assertEquals(expectedResponse, result);
        verify(userService, times(1)).getById(userId);
    }

    @Test
    @DisplayName("Test deleteById if user is found")
    public void testDeleteById_ifUserIsFound() {
        long userId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.OK);
        when(userService.deleteById(userId)).thenReturn(expectedResponse);

        ResponseEntity result = userController.deleteById(userId);

        assertEquals(expectedResponse, result);
        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Test deleteById if user not found")
    public void testDeleteById_ifUserNotFound() {
        long userId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.NOT_FOUND);
        when(userService.deleteById(userId)).thenReturn(expectedResponse);

        ResponseEntity result = userController.deleteById(userId);

        assertEquals(expectedResponse, result);
        verify(userService, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Test updateUser if user is found")
    public void testUpdateUser_ifUserIsFound() {
        long userId = 1L;
        Set<RoleType> roles = new HashSet<>();
        User user = new User(userId, "test1", "test1@example.com", "password1", roles);
        ResponseEntity<User> expectedResponse = new ResponseEntity<>(user, HttpStatus.OK);
        when(userService.update(userId, user)).thenReturn(expectedResponse);

        ResponseEntity result = userController.updateUser(userId, user);

        assertEquals(expectedResponse, result);
        verify(userService, times(1)).update(userId, user);
    }

    @Test
    @DisplayName("Test updateUser if user not found")
    public void testUpdateUser_ifUserNotFound() {
        long userId = 1L;
        Set<RoleType> roles = new HashSet<>();
        User user = new User(userId, "test1", "test1@example.com", "password1", roles);
        ResponseEntity<User> expectedResponse = new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        when(userService.update(userId, user)).thenReturn(expectedResponse);

        ResponseEntity result = userController.updateUser(userId, user);

        assertEquals(expectedResponse, result);
        verify(userService, times(1)).update(userId, user);
    }
}
