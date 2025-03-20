package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.dto.CommentDto;
import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TaskDto> getAllTask() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity getTaskById(@PathVariable long id) {
        return taskService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteUserById(@PathVariable long id) {
        return taskService.deleteById(id);
    }

    @PostMapping()
    public ResponseEntity createTask(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTask(@PathVariable long id, @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskDto);
    }

    @GetMapping("/my")
    public List<TaskDto> getAllTaskByExecutor() {
        return taskService.getAllByExecutorId();
    }

    @PutMapping("/add-comment/{id}")
    public ResponseEntity addComment(@PathVariable long id, @RequestBody CommentDto commentDto) {
        return taskService.addComment(id, commentDto);
    }
}
