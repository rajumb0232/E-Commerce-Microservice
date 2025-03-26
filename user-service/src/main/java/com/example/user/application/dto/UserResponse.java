package com.example.user.application.dto;

import com.example.user.domain.model.UserRole;

public record UserResponse(
        Long userId,
        String username,
        String email,
        UserRole role,
        Long createdAt,
        Long updatedAt
) {
}
