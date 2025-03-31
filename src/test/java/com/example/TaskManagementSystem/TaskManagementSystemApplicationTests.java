package com.example.TaskManagementSystem;

import com.example.TaskManagementSystem.model.PriorityTask;
import com.example.TaskManagementSystem.model.RoleType;
import com.example.TaskManagementSystem.model.StatusTask;
import com.example.TaskManagementSystem.model.entity.Task;
import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.TaskRepository;
import com.example.TaskManagementSystem.repository.UserRepository;
import com.example.TaskManagementSystem.service.TaskService;
import com.example.TaskManagementSystem.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskManagementSystemApplicationTests {

    @LocalServerPort
    private Integer port;

    private TestRestTemplate template = new TestRestTemplate();

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    @BeforeAll
    public static void beforeAll() {
        container.start();
    }

    @AfterAll
    public static void afterAll() {
        container.stop();
    }

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    @DisplayName("test get connection with database")
    public void testConnectionWithDB() {
        Long userId = 1L;
        Set<RoleType> role = new HashSet<>();
        role.add(RoleType.ROLE_ADMIN);
        User user = new User(userId, "admin", "admin@mail.ru", "password", role);

        Long taskId = 1L;
        List<String> comments = new ArrayList<>();
        comments.add("first comments");
        Task task = new Task(1L, "title", StatusTask.PENDING, PriorityTask.LOW, comments, user, user);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        ResponseEntity resultForUser = userService.getById(userId);
        ResponseEntity resultForTask = taskService.getById(userId);

        assertEquals(HttpStatus.OK, resultForUser.getStatusCode());
        assertEquals(HttpStatus.OK, resultForTask.getStatusCode());
        verify(userRepository, times(1)).findById(userId);
        verify(taskRepository, times(1)).findById(userId);
    }
}
