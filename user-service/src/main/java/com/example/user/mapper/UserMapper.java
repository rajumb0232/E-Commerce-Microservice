package com.example.user.mapper;

import com.example.user.model.User;
import com.example.user.requestdto.RegistrationRequest;
import com.example.user.responsedto.UserResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UserMapper {

    public User mapToNewUser(RegistrationRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
