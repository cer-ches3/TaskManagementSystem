package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAllUser() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable long id) {
        return userService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity createUser(@RequestBody User user) {
        return userService.create(user);
    }

    @PutMapping("/update")
    public ResponseEntity updateUser(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable long id) {
        return userService.deleteById(id);
    }
}
