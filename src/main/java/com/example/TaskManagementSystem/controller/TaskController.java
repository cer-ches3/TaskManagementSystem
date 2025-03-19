package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    public List<TaskDto> getAllUser() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getTaskById(@PathVariable long id) {
        return taskService.getById(id);
    }

    @PostMapping()
    public ResponseEntity createUser(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@PathVariable long id, @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUserById(@PathVariable long id) {
        return taskService.deleteById(id);
    }
}
