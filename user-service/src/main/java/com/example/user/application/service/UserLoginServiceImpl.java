package com.example.user.application.service;

import com.example.user.application.repository.UserRepository;
import com.example.user.application.service.contracts.UserLoginService;
import com.example.user.application.dto.AuthRecord;
import com.example.user.application.dto.LoginRequest;
import com.example.user.domain.exception.InvalidCredentialsException;
import com.example.user.domain.model.User;
import com.example.user.shared.config.Env;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Service class responsible for handling authentication operations.
 * It provides methods for authenticating users, generating JWT tokens,
 * and setting them in HTTP cookies.
 */
@Service
@AllArgsConstructor
@Slf4j
public class UserLoginServiceImpl implements UserLoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Env env;

    /**
     * Authenticates the user based on the provided login request.
     *
     * @param request the login request containing email and password
     * @return an AuthRecord containing user details and token metadata
     * @throws InvalidCredentialsException if authentication fails
     */
    @Override
    public AuthRecord authenticate(LoginRequest request) {
        log.info("Authenticating user with email: {}", request.email());
        authenticateRequest(request);

        var user = userRepository.findByEmail(request.email())
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
        Instant now = Instant.now();
        Instant accessExpiration = now.plusSeconds(env.getSecurity().getTokenValidity().getAccessValidity());
        Instant refreshExpiration = now.plusSeconds(env.getSecurity().getTokenValidity().getRefreshValidity());

        return new AuthRecord(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                true,
                accessExpiration.toEpochMilli(),
                refreshExpiration.toEpochMilli()
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
