package com.example.user.infrastructure.mapping;

import com.example.user.domain.model.User;
import com.example.user.application.dto.UserResponse;
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
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}