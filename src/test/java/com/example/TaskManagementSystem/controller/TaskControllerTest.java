package com.example.TaskManagementSystem.controller;

import com.example.TaskManagementSystem.model.PriorityTask;
import com.example.TaskManagementSystem.model.StatusTask;
import com.example.TaskManagementSystem.model.dto.CommentDto;
import com.example.TaskManagementSystem.model.dto.TaskDto;
import com.example.TaskManagementSystem.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test getAllTask")
    public void testGetAllTask() {
        List<TaskDto> tasks = new ArrayList<>();
        tasks.add(new TaskDto(1L, "Title1", StatusTask.PENDING, PriorityTask.LOW, new ArrayList<>(), 1L, 1L));
        when(taskService.getAll()).thenReturn(tasks);

        List<TaskDto> result = taskController.getAllTask();

        assertEquals(1, result.size());
        assertEquals("Title1", result.get(0).getTitle());
        assertEquals(StatusTask.PENDING, result.get(0).getStatusTask());
        assertEquals(PriorityTask.LOW, result.get(0).getPriorityTask());
        assertEquals(1L, result.get(0).getAuthorId());
        assertEquals(1L, result.get(0).getExecutorId());
        verify(taskService, times(1)).getAll();
    }

    @Test
    @DisplayName("Test getTaskById if task is found")
    public void testGetTaskById_ifTaskIsFound() {
        Long taskId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.OK);
        when(taskService.getById(taskId)).thenReturn(expectedResponse);

        ResponseEntity result = taskController.getTaskById(taskId);

        assertEquals(expectedResponse, result);
        verify(taskService, times(1)).getById(taskId);
    }

    @Test
    @DisplayName("Test getTaskById if task not found")
    public void testGetTaskById_ifTaskNotFound() {
        Long taskId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.NOT_FOUND);
        when(taskService.getById(taskId)).thenReturn(expectedResponse);

        ResponseEntity result = taskController.getTaskById(taskId);

        assertEquals(expectedResponse, result);
        verify(taskService, times(1)).getById(taskId);
    }

    @Test
    @DisplayName("Test deleteUserById if task is found")
    public void testDeleteUserById_ifTaskIsFound() {
        Long taskId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.OK);
        when(taskService.deleteById(taskId)).thenReturn(expectedResponse);

        ResponseEntity result = taskController.deleteUserById(taskId);

        assertEquals(expectedResponse, result);
        verify(taskService, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Test deleteUserById if task not found")
    public void testDeleteUserById_ifTaskNotFound() {
        Long taskId = 1L;
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.NOT_FOUND);
        when(taskService.deleteById(taskId)).thenReturn(expectedResponse);

        ResponseEntity result = taskController.deleteUserById(taskId);

        assertEquals(expectedResponse, result);
        verify(taskService, times(1)).deleteById(taskId);
    }

    @Test
    @DisplayName("Test createTask")
    public void testCreateTask() {
        TaskDto taskDto = new TaskDto();
        ResponseEntity<String> expectedResponse = new ResponseEntity<>("Task created", HttpStatus.CREATED);
        when(taskService.create(taskDto)).thenReturn(expectedResponse);

        ResponseEntity<?> actualResponse = taskController.createTask(taskDto);

        assertEquals(expectedResponse, actualResponse);
        verify(taskService, times(1)).create(taskDto);
    }

    @Test
    @DisplayName("Test updateTask if task is found")
    public void testUpdateTask_ifTaskIsFound() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.OK);
        when(taskService.update(taskId, taskDto)).thenReturn(expectedResponse);

        ResponseEntity result = taskController.updateTask(taskId, taskDto);

        assertEquals(expectedResponse, result);
        verify(taskService, times(1)).update(taskId, taskDto);
    }

    @Test
    @DisplayName("Test updateTask if task not found")
    public void testUpdateTask_ifTaskNotFound() {
        Long taskId = 1L;
        TaskDto taskDto = new TaskDto();
        ResponseEntity expectedResponse = new ResponseEntity(HttpStatus.NOT_FOUND);
        when(taskService.update(taskId, taskDto)).thenReturn(expectedResponse);

        ResponseEntity result = taskController.updateTask(taskId, taskDto);

        assertEquals(expectedResponse, result);
        verify(taskService, times(1)).update(taskId, taskDto);
    }

    @Test
    @DisplayName("Test getAllTaskByExecutor")
    public void testGetAllTaskByExecutor() {
        List<TaskDto> tasks = new ArrayList<>();
        tasks.add(new TaskDto());
        when(taskService.getAllByExecutorId()).thenReturn(tasks);

        List<TaskDto> result = taskController.getAllTaskByExecutor();

        assertEquals(1, result.size());
        verify(taskService, times(1)).getAllByExecutorId();
    }

    @Test
    @DisplayName("Test addComment if task is found")
    public void testAddComment_ifTaskIsFound() {
        Long taskId = 1L;
        CommentDto commentDto = new CommentDto();
        ResponseEntity expectedRequest = new ResponseEntity(HttpStatus.OK);
        when(taskService.addComment(taskId, commentDto)).thenReturn(expectedRequest);

        ResponseEntity result = taskController.addComment(taskId, commentDto);

        assertEquals(expectedRequest, result);
        verify(taskService, times(1)).addComment(taskId, commentDto);
    }
}
