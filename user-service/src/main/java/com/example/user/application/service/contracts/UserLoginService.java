package com.example.user.application.service.contracts;

import com.example.user.application.dto.AuthRecord;
import com.example.user.application.dto.LoginRequest;
import com.example.user.domain.exception.InvalidCredentialsException;

/**
 * Service interface for user authentication.
 */
public interface UserLoginService {

    /**
     * Authenticates the user based on the provided login request.
     *
     * @param request the login request containing email and password
     * @return an AuthRecord containing user details and token metadata
     * @throws InvalidCredentialsException if authentication fails
     */
    AuthRecord authenticate(LoginRequest request) throws InvalidCredentialsException;
}
