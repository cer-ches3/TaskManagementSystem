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
public class UserService implements CRUDService<User> {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public ResponseEntity getById(long id) {
        User existsUser = userRepository.findById(id).orElse(null);

        if (existsUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("User with id: {0} not found!", id));
        }

        return ResponseEntity.status(HttpStatus.OK).body(existsUser);
    }

    @Override
    public ResponseEntity create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageFormat.format("Email {0} is already in use!", user.getEmail()));
        }

        User newUser = new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRoleUser(user.getRoleUser());
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(MessageFormat.format("Created user with email {0}.", user.getEmail()));
    }

    @Override
    public ResponseEntity update(User user) {
        User existsUser = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (existsUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("User with email {0} not found!", user.getEmail()));
        }

        existsUser.setPassword(user.getPassword());
        existsUser.setRoleUser(user.getRoleUser());
        userRepository.save(existsUser);

        return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("Update user with email {0}.", user.getEmail()));

    }

    @Override
    public ResponseEntity deleteById(long id) {
        User existsUser = userRepository.findById(id).orElse(null);

        if (existsUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageFormat.format("User with id: {0} not found!", id));
        }

        userRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(MessageFormat.format("User with id {0} is deleted.", id));
    }
}
