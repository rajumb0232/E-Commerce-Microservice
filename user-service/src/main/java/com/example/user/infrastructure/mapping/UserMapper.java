package com.example.user.infrastructure.mapping;

import com.example.user.api.dto.AuthRecord;
import com.example.user.domain.model.User;
import com.example.user.api.dto.UserResponse;
import org.springframework.stereotype.Component;

/**
 * A simple mapper for converting User objects to its corresponding DTOs and vice versa.
 */
@Component
public class UserMapper {

    /**
     * Converts a User to a UserResponse.
     *
     * @param user the User data
     * @return the corresponding UserResponse
     */
    public UserResponse mapToUserResponse(User user) {

    }

    /**
     * Converts a User to an AuthRecord.
     *
     * @param user the User data
     * @return the corresponding AuthRecord
     */
    public AuthRecord mapToAuthResponse(User user) {
        return null;
    }
}