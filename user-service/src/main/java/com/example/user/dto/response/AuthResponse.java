package com.example.user.dto.response;

import com.example.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private Long userId;
    private String username;
    private String email;
    private UserRole role;
    private Long accessExpiration;
    private Long refreshExpiration;
}
