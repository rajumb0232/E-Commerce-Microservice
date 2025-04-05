package com.example.user.api;

import com.example.user.application.service.UserLoginServiceImpl;
import com.example.user.application.service.contracts.LoginCredentialGenerator;
import com.example.user.application.service.contracts.UserRegistrationService;
import com.example.user.application.dto.AuthRecord;
import com.example.user.application.dto.LoginRequest;
import com.example.user.application.dto.RegistrationRequest;
import com.example.user.application.dto.UserResponse;
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

    private final LoginCredentialGenerator authenticateService;
    private final UserLoginServiceImpl userLoginService;
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody @Valid RegistrationRequest request) {
        UserResponse response = userRegistrationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthRecord> loginUser(@RequestBody @Valid LoginRequest request) {
        AuthRecord authRecord = userLoginService.authenticate(request);
        HttpHeaders headers = authenticateService.grantAccessAndRefreshTokenCookies(authRecord);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(authRecord);
    }
}
