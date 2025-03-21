package com.example.TaskManagementSystem.web.model;

import com.example.TaskManagementSystem.model.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String username;
    private String email;
    private Set<RoleType> roles;
    private String password;

    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                ", password='" + password + '\'';
    }
}
