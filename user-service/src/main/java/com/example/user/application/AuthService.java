package com.example.user.application;

import com.example.user.api.dto.AuthRecord;
import com.example.user.api.dto.LoginRequest;
import com.example.user.domain.exception.InvalidCredentialsException;
import com.example.user.infrastructure.config.Env;
import com.example.user.infrastructure.mapping.UserMapper;
import com.example.user.domain.model.User;
import com.example.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    private final TokenGenerationService tokenGenerationService;
    private final Env env;

    /**
     * Authenticates the user based on the provided login request.
     *
     * @param request the login request containing email and password
     * @return an AuthRecord containing user details and token metadata
     * @throws InvalidCredentialsException if authentication fails
     */
    public AuthRecord authenticate(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.email());
        authenticateRequest(request);

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        return generateAuthRecord(user);
    }

    /**
     * Generates an AuthRecord containing user details and token metadata.
     * <p>
     *
     * @param user the user object
     * @return an AuthRecord containing user details and token metadata
     */
    private AuthRecord generateAuthRecord(User user) {
        long nowMillis = System.currentTimeMillis();
        Date issueDateTime = new Date(nowMillis);
        Date accessExpiration = new Date(nowMillis + env.getSecurity().getTokenValidity().getAccessValidity());
        Date refreshExpiration = new Date(nowMillis + env.getSecurity().getTokenValidity().getRefreshValidity());

        return new AuthRecord(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                true,
                issueDateTime.getTime(),
                accessExpiration.getTime(),
                refreshExpiration.getTime()
        );
    }

    /**
     * Uses the AuthenticationManager to validate the user's credentials.
     *
     * @param request the login request
     * @throws InvalidCredentialsException if the credentials are invalid
     */
    private void authenticateRequest(LoginRequest request) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(request.email(), request.password());
        try {
            authenticationManager.authenticate(token);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for email: {}", request.email());
            throw new InvalidCredentialsException("Invalid credentials");
        }
    }
}
