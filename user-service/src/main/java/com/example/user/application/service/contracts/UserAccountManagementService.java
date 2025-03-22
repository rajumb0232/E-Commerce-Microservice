package com.example.user.application.service.contracts;

import com.example.user.application.dto.UserRequest;
import com.example.user.application.dto.UserResponse;

/**
 * Service interface for user account management.
 */
public interface UserAccountManagementService {

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return the user response
     */
    UserResponse findUserById(Long id);

    /**
     * Updates a user's information.
     *
     * @param id      the user ID
     * @param request the user request containing updated information
     * @return the updated user response
     */
    UserResponse updateUser(Long id, UserRequest request);

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID
     */
    void deleteUser(Long id);
}
