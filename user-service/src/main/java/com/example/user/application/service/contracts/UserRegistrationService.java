package com.example.user.application.service.contracts;

import com.example.user.application.dto.RegistrationRequest;
import com.example.user.application.dto.UserResponse;

/**
 * Service interface for user registration.
 */
public interface UserRegistrationService {

    /**
     * Registers a new user.
     * @param request the registration request containing username, email, role and password
     * @return the registered user's response
     */
    UserResponse register(RegistrationRequest request);
}
