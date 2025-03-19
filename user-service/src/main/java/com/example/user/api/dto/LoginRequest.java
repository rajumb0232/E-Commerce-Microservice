package com.example.user.api.dto;

import com.example.user.api.dto.rules.Email;
import com.example.user.api.dto.rules.Password;

public record LoginRequest(
        @Email String email,
        @Password String password
) {
}
