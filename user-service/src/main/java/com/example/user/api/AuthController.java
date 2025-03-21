package com.example.user.api;

import com.example.user.application.dto.AuthRecord;
import com.example.user.application.dto.LoginRequest;
import com.example.user.application.AuthService;
import com.example.user.application.TokenGenerationService;
import jakarta.validation.Valid;
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
    public ResponseEntity<AuthRecord> loginUser(@RequestBody @Valid LoginRequest request) {
        AuthRecord authRecord = authService.authenticate(request);
        HttpHeaders headers = tokenGenerationService.grantAccessAndRefreshTokenCookies(authRecord);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(authRecord);
    }
}
