package com.example.TaskManagementSystem.model.dto;

import com.example.TaskManagementSystem.model.PriorityTask;
import com.example.TaskManagementSystem.model.StatusTask;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class TaskDto {
    private Long id;
    private String title;
    private StatusTask statusTask;
    private PriorityTask priorityTask;
    private List<String> comments;
    private Long authorId;
    private Long executorId;
}
