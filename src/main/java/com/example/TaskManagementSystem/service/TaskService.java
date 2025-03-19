package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.model.entity.Task;
import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.TaskRepository;
import com.example.TaskManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public ResponseEntity getById(long id) {
        Task existsTask = taskRepository.findById(id).orElse(null);

        if (existsTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Task with ID: {0} not found!", id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(mapToDto(existsTask));
    }

    public ResponseEntity create(TaskDto taskDto) {
        Task newTask = mapToEntity(taskDto);
        Long authorId = taskDto.getAuthorId();
        Long executorId = taskDto.getExecutorId();
        User author = userRepository.findById(authorId).orElse(null);
        User executor = userRepository.findById(executorId).orElse(null);

        if (author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Author with ID: {0} not found!", authorId));
        }
        if (executor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Executor with ID: {0} not found!", executorId));
        }

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

        existsTask.setStatusTask(taskDto.getStatusTask());
        existsTask.setPriorityTask(taskDto.getPriorityTask());
        existsTask.setComments(taskDto.getComments());

        Long authorId = taskDto.getAuthorId();
        Long executorId = taskDto.getExecutorId();
        User author = userRepository.findById(authorId).orElse(null);
        User executor = userRepository.findById(executorId).orElse(null);

        if (author == null || executor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Author or executor with ID: {0} not found!", authorId));
        }

        existsTask.setAuthor(author);
        existsTask.setExecutor(executor);
        taskRepository.save(existsTask);

        return ResponseEntity.status(HttpStatus.OK).body(mapToDto(existsTask));
    }

    public ResponseEntity deleteById(long id) {
        Task extendsTask = taskRepository.findById(id).orElse(null);

        if (extendsTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("Task with ID: {0} not found!", id));
        }

        taskRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("Task with ID: {0} is deleted", id));
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

    private boolean isExistsTask(TaskDto taskDto) {
        return taskRepository.findTaskByTitle(taskDto.getTitle()).isPresent();
    }
}
