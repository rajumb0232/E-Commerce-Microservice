package com.example.user.api.dto;

import com.example.user.domain.model.UserRole;

public record AuthRecord(
        Long userId,
        String username,
        String email,
        UserRole role,
        Boolean authenticated,
        Long issueAt,
        Long accessExpiration,
        Long refreshExpiration
) {
}
