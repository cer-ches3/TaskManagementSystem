package com.example.TaskManagementSystem.service;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CRUDService<T> {
    List<T> getAll();

    ResponseEntity getById(long id);

    ResponseEntity create(T item);

    ResponseEntity update(T item);

    ResponseEntity deleteById(long id);
}
