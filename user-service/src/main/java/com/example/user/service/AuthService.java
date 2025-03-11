package com.example.user.service;

import com.example.user.dto.request.LoginRequest;
import com.example.user.dto.response.AuthResponse;
import com.example.user.exception.InvalidCredentialsException;
import com.example.user.mapper.UserMapper;
import com.example.user.model.User;
import com.example.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Service class responsible for handling authentication operations.
 * It provides methods for authenticating users, generating JWT tokens,
 * and setting them in HTTP cookies.
 */
@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    /**
     * Authenticates the user based on the provided login request.
     *
     * @param request the login request containing email and password
     * @return an AuthResponse containing user details and token metadata
     * @throws InvalidCredentialsException if authentication fails
     */
    public AuthResponse authenticate(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.getEmail());
        authenticateRequest(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        return userMapper.mapToAuthResponse(user);
    }

    /**
     * Uses the AuthenticationManager to validate the user's credentials.
     *
     * @param request the login request
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    private void authenticateRequest(LoginRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }

}
