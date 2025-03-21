package com.example.user.application.dto;

import com.example.user.domain.model.UserRole;

public record AuthRecord(
        Long userId,
        String username,
        String email,
        UserRole role,
        Boolean authenticated,
        Long accessExpiration,
        Long refreshExpiration
) {
}
