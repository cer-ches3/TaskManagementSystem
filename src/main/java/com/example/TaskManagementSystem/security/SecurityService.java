package com.example.TaskManagementSystem.security;

import com.example.TaskManagementSystem.model.entity.User;
import com.example.TaskManagementSystem.repository.UserRepository;
import com.example.TaskManagementSystem.security.jwt.JwtUtils;
import com.example.TaskManagementSystem.web.model.AuthResponse;
import com.example.TaskManagementSystem.web.model.CreateUserRequest;
import com.example.TaskManagementSystem.web.model.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AuthResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return AuthResponse.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    public void register(CreateUserRequest createUserRequest) {
        var user = User.builder()
                .username(createUserRequest.getUsername())
                .email(createUserRequest.getEmail())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .build();
        user.setRoles(createUserRequest.getRoles());

        userRepository.save(user);
    }
}
