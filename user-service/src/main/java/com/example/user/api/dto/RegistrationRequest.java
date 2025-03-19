package com.example.user.api.dto;

import com.example.user.api.dto.rules.Password;
import com.example.user.api.dto.rules.Username;
import com.example.user.domain.model.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @Username String username,
        @Email String email,
        @Password String password,
        @NotNull(message = "User role is required") UserRole role
) {
}
