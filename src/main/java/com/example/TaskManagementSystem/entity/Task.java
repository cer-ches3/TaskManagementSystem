package com.example.TaskManagementSystem.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_task")
    private StatusTask statusTask;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority_task")
    private PriorityTask priorityTask;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @ManyToOne
    private User author;

    @ManyToOne
    private User executor;
}
