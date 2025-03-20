package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "Контроллер для работы с пользователями.")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение всех пользователей.",
            description = "Метод,доступный только администратору, позволяющий получить список всех пользователей системы.")
    public List<User> getAllUser() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение пользователя по id.",
            description = "Метод,доступный только администратору, позволяющий получить пользователя по его id.")
    public ResponseEntity getUserById(@PathVariable long id) {
        return userService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление пользователя по id.",
            description = "Метод,доступный только администратору, позволяющий удалить пользователя по его id.")
    public ResponseEntity deleteById(@PathVariable long id) {
        return userService.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновление пользователя.",
            description = "Метод,доступный только администратору, позволяющий изменять данные пользователя по его id.")
    public ResponseEntity updateUser(@PathVariable long id, @RequestBody User user) {
        return userService.update(id, user);
    }
}
