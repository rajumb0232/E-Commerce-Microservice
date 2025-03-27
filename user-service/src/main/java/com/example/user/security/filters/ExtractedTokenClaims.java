package com.example.user.security.filters;

import com.example.user.application.dto.rules.Email;
import com.example.user.application.dto.rules.Username;
import com.example.user.domain.model.UserRole;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ExtractedTokenClaims(
        @Username String username,
        @Email String email,
        @NotNull UserRole role,
        long accessExpiration,
        long refreshExpiration
) {
    /**
     * Compact constructor with validation logic.
     * Validates all fields before the canonical constructor is called.
     *
     * @throws IllegalArgumentException if any validation fails
     */
    public ExtractedTokenClaims {
        // Validate expiration times
        long currentTimeMillis = Instant.now().toEpochMilli();

        if (accessExpiration <= currentTimeMillis) {
            throw new IllegalArgumentException("Access token has expired");
        }

        if (refreshExpiration <= currentTimeMillis) {
            throw new IllegalArgumentException("Refresh token has expired");
        }
    }
}
