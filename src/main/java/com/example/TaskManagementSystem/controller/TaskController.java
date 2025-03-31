package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.dto.CommentDto;
import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "TaskController", description = "Контроллер для работы с задачами.")
public class TaskController {
    private final TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение списка всех задач.",
            description = "Метод,доступный только администратору, позволяющий получить список задач всех пользователей.")
    public List<TaskDto> getAllTask() {
        return taskService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение задачи по id.",
            description = "Метод,доступный только администратору, позволяющий получить задачу по её id.")
    public ResponseEntity getTaskById(@PathVariable long id) {
        return taskService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление задачи по id.",
            description = "Метод,доступный только администратору, позволяющий удалить задачу по её id.")
    public ResponseEntity deleteUserById(@PathVariable long id) {
        return taskService.deleteById(id);
    }

    @PostMapping()
    @Operation(summary = "Создание новой задачи",
            description = "Метод,доступный только авторизованным пользователям, позволяющий создать новую задачу")
    public ResponseEntity createTask(@RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновление задачи.",
            description = "Метод,доступный только авторизованным пользователям, позволяющий изменить какие-либо данные в задаче по её id," +
                    "при условии, что пользователь является её исполнителем либо администратором.")
    public ResponseEntity updateTask(@PathVariable long id, @RequestBody TaskDto taskDto) {
        return taskService.update(id, taskDto);
    }

    @GetMapping("/my")
    @Operation(summary = "Получение своих задач.",
            description = "Метод,доступный только авторизованным пользователям, позволяющий получить список задач," +
                    "в которых пользователь назначен исполнителем.")
    public List<TaskDto> getAllTaskByExecutor() {
        return taskService.getAllByExecutorId();
    }

    @PutMapping("/add-comment/{id}")
    @Operation(summary = "Добавление комментария.",
            description = "Метод,доступный только авторизованным пользователям, позволяющий добавлять комментарии к задаче" +
                    "при условии, что пользователь является её исполнителем либо администратором.")
    public ResponseEntity addComment(@PathVariable long id, @RequestBody CommentDto commentDto) {
        return taskService.addComment(id, commentDto);
    }
}
