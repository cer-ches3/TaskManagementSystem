package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.model.dto.CommentDto;
import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.model.entity.Task;
import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.TaskRepository;
import com.example.TaskManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskDto> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskService::mapToDto)
                .toList();
    }

    public List<TaskDto> getAllByExecutorId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String executorName = authentication.getName();
        Long executorId = userRepository.findByUsername(executorName).orElse(null).getId();

        return taskRepository.findAll()
                .stream()
                .filter(t -> t.getExecutor().getId().equals(executorId))
                .map(TaskService::mapToDto)
                .toList();
    }

    public ResponseEntity getById(long id) {
        Task existsTask = taskRepository.findById(id).orElse(null);

        if (existsTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Task with ID: {0} not found!", id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(mapToDto(existsTask));
    }

    public ResponseEntity create(TaskDto taskDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User author = userRepository.findByUsername(username).orElse(null);

        User executor = userRepository.findById(taskDto.getExecutorId()).orElse(null);

        if (executor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Executor with ID: {0} not found!", taskDto.getExecutorId()));
        }

        Task newTask = mapToEntity(taskDto);
        newTask.setAuthor(author);
        newTask.setExecutor(executor);
        taskRepository.save(newTask);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToDto(newTask));
    }

    public ResponseEntity update(Long id, TaskDto taskDto) {
        Task existsTask = taskRepository.findById(id).orElse(null);

        if (existsTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Task with Id: {0} not found!", id));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String executorNameFromContext = authentication.getName();

        if (isAdmin(executorNameFromContext) || isExecutor(executorNameFromContext, existsTask)) {
            existsTask.setStatusTask(taskDto.getStatusTask());
            existsTask.setPriorityTask(taskDto.getPriorityTask());
            existsTask.setComments(taskDto.getComments());

            Long executorId = taskDto.getExecutorId();
            User executor = userRepository.findById(executorId).orElse(null);

            if (executor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Executor with ID: {0} not found!", executorId));
            }

            existsTask.setExecutor(executor);
            taskRepository.save(existsTask);

            return ResponseEntity.status(HttpStatus.OK).body(mapToDto(existsTask));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Full authentication is required to access this resource!");
    }

    public ResponseEntity deleteById(long id) {
        if (!taskRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Task with ID: {0} not found!", id));
        }

        taskRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("Task with ID: {0} is deleted.", id));
    }

    public ResponseEntity addComment(long id, CommentDto commentDto) {
        Task existsTask = taskRepository.findById(id).orElse(null);

        if (existsTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Task with ID: {0} not found!", id));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String executorNameFromContext = authentication.getName();

        if (isAdmin(executorNameFromContext) || isExecutor(executorNameFromContext, existsTask)) {
            List<String> comments = new java.util.ArrayList<>(existsTask.getComments().stream().toList());
            comments.add(commentDto.getComment());

            existsTask.setComments(comments);
            taskRepository.save(existsTask);

            return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("Added comment {0} for task {1}.", commentDto.getComment(), id));
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Full authentication is required to access this resource!");
    }

    private static Task mapToEntity(TaskDto taskDto) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setTitle(taskDto.getTitle());
        task.setStatusTask(taskDto.getStatusTask());
        task.setPriorityTask(taskDto.getPriorityTask());
        task.setComments(taskDto.getComments());

        return task;
    }

    private static TaskDto mapToDto(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId());
        taskDto.setTitle(task.getTitle());
        taskDto.setStatusTask(task.getStatusTask());
        taskDto.setPriorityTask(task.getPriorityTask());
        taskDto.setComments(task.getComments());
        taskDto.setAuthorId(task.getAuthor().getId());
        taskDto.setExecutorId(task.getExecutor().getId());

        return taskDto;
    }

    private boolean isExecutor(String executorNameFromContext, Task existsTask) {
        Long executorIdFromContext = userRepository.findByUsername(executorNameFromContext).orElse(null).getId();
        Long executorIdFromTask = existsTask.getExecutor().getId();

        return executorIdFromTask.equals(executorIdFromContext);
    }

    private boolean isAdmin(String executorNameFromContext) {
        String role = userRepository.findByUsername(executorNameFromContext).get().getRoles()
                .stream().
                findFirst().
                toString();

        return role.contains("ROLE_ADMIN");
    }
}
