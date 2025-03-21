package com.example.user.application.dto;

import com.example.user.application.dto.rules.Email;
import com.example.user.application.dto.rules.Password;

public record LoginRequest(
        @Email String email,
        @Password String password
) {
}
