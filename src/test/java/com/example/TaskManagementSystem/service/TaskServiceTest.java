package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.model.PriorityTask;
import com.example.TaskManagementSystem.model.StatusTask;
import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.model.entity.Task;
import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.TaskRepository;
import com.example.TaskManagementSystem.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    @DisplayName("Test getAll")
    public void testGetAll() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Title1", StatusTask.PENDING, PriorityTask.LOW, new ArrayList<>(), new User(), new User()));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDto> result = taskService.getAll();

        assertEquals(1, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals(StatusTask.PENDING, result.get(0).getStatusTask());
        assertEquals(PriorityTask.LOW, result.get(0).getPriorityTask());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test getAllById")
    public void testGetAllById() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        Task task1 = new Task();
        task1.setId(1L);
        User user1 = new User();
        user1.setId(user.getId());
        task1.setExecutor(user1);
        task1.setAuthor(user1);

        Task task2 = new Task();
        task2.setId(2L);
        User user2 = new User();
        user2.setId(user.getId());
        task2.setExecutor(user2);
        task2.setAuthor(user2);

        List<Task> tasks = Arrays.asList(task1, task2);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDto> result = taskService.getAllByExecutorId();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(userRepository, times(1)).findByUsername(user.getUsername());
        verify(taskRepository, times(1)).findAll();
        verify(securityContext, times(1)).getAuthentication();
        verify(authentication, times(1)).getName();
    }

    @Test
    @DisplayName("Test getById if task is found")
    public void testGetById_ifTaskIsFound() {
        Long taskId = 1L;
        Task task = new Task(taskId, "title", StatusTask.PENDING, PriorityTask.LOW, new ArrayList<>(), new User(), new User());
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        ResponseEntity result = taskService.getById(taskId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Test getById if task not found")
    public void testGetById_ifTaskNotFound() {
        Long taskId = 1L;
        //Task task = new Task(taskId, "title", StatusTask.PENDING, PriorityTask.LOW, new ArrayList<>(), new User(), new User());
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        ResponseEntity result = taskService.getById(taskId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    @DisplayName("Test createTask if success")
    public void testCreateTask_ifSuccess() {
        TaskDto taskDto = new TaskDto();
        taskDto.setExecutorId(1L);

        User author = new User();
        author.setUsername("authorUser");

        User executor = new User();
        executor.setId(1L);

        when(authentication.getName()).thenReturn("authorUser");
        when(userRepository.findByUsername("authorUser")).thenReturn(Optional.of(author));
        when(userRepository.findById(1L)).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity result = taskService.create(taskDto);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    @DisplayName("Test createTask if executor not found")
    public void testCreateTask_ifExecutorNotFound() {
        TaskDto taskDto = new TaskDto();
        taskDto.setExecutorId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity result = taskService.create(taskDto);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(MessageFormat.format("Executor with ID: {0} not found!", taskDto.getExecutorId()), result.getBody());
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    @DisplayName("Test deleteById if task is found")
    public void testDeleteById_ifTaskIsFound() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(true);

        ResponseEntity result = taskService.deleteById(taskId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(MessageFormat.format("Task with ID: {0} is deleted.", taskId), result.getBody());
        verify(taskRepository, times(1)).existsById(taskId);
    }

    @Test
    @DisplayName("Test deleteById if task not found")
    public void testDeleteById_ifTaskNotFound() {
        Long taskId = 1L;
        when(taskRepository.existsById(taskId)).thenReturn(false);

        ResponseEntity result = taskService.deleteById(taskId);

        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(MessageFormat.format("Task with ID: {0} not found!", taskId), result.getBody());
        verify(taskRepository, times(1)).existsById(taskId);
    }
}

