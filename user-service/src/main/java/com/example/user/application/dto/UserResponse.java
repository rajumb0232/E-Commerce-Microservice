package com.example.user.application.dto;

import com.example.user.domain.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public record UserResponse(
        Long userId,
        String username,
        String email,
        UserRole role,
        LocalDate createdAt,
        LocalDate updatedAt
) {
}
