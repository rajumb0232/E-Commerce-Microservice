package com.example.user.api.dto;

import com.example.user.api.dto.rules.Email;
import com.example.user.api.dto.rules.Username;

public record UserRequest(
        @Username String username,
        @Email String email
) {
}
