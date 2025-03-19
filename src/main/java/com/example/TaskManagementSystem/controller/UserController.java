package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUser() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getUserById(@PathVariable long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteById(@PathVariable long id) {
        return userService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable long id, @RequestBody User user) {
        return userService.update(id, user);
    }
}
