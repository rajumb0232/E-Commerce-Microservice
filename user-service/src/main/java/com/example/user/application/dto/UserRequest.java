package com.example.user.application.dto;

import com.example.user.application.dto.rules.Email;
import com.example.user.application.dto.rules.Username;

public record UserRequest(
        @Username String username,
        @Email String email
) {
}
