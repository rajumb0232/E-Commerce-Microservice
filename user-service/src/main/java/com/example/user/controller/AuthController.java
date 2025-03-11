package com.example.user.controller;

import com.example.user.dto.request.LoginRequest;
import com.example.user.dto.response.AuthResponse;
import com.example.user.service.AuthService;
import com.example.user.service.TokenGenerationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("${app.base-url}")
public class AuthController {

    private final AuthService authService;
    private final TokenGenerationService tokenGenerationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        AuthResponse response = authService.authenticate(request);
        HttpHeaders headers = tokenGenerationService.getLoginCredentials(response);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(response);
    }
}
