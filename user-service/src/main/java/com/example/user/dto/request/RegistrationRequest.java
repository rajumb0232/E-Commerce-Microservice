package com.example.user.dto.request;

import com.example.user.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private UserRole role;
}
