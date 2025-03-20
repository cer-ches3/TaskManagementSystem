package com.example.TaskManagementSystem.service;

import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public ResponseEntity getById(long id) {
        User existsUser = userRepository.findById(id).orElse(null);

        if (existsUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("User with id: {0} not found!", id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(existsUser);
    }

    public ResponseEntity deleteById(long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("User with id: {0} not found!", id));
        }

        userRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("User with id {0} is deleted.", id));
    }

    public ResponseEntity update(Long id, User user) {
        User existsUser = userRepository.findById(id).orElse(null);

        if (existsUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("User with ID {0} not found!", id));
        }

        existsUser.setUsername(user.getUsername());
        existsUser.setEmail(user.getEmail());
        existsUser.setRoles(user.getRoles());
        userRepository.save(existsUser);

        return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("Update user with ID {0}.", id));
    }
}
