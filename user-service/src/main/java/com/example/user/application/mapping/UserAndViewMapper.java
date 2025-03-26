package com.example.user.application.mapping;

import com.example.user.domain.model.User;
import com.example.user.application.dto.UserResponse;
import org.mapstruct.Mapper;

import java.time.Instant;

/**
 * A simple mapper for converting User objects to its corresponding DTOs and vice versa.
 */
@Mapper(componentModel = "spring")
public interface UserAndViewMapper {

    /**
     * Converts a User to a UserResponse.
     *
     * @param user the User data
     * @return the corresponding UserResponse
     */
    UserResponse mapToUserResponse(User user);

    /**
     * Helper method to covert the long timeEpochMillis to an Instant object.
     *
     * @param timeEpochMillis the timeEpochMillis
     * @return the Instant object
     */
    default Instant convertToInstant(Long timeEpochMillis) {
        return Instant.ofEpochMilli(timeEpochMillis);
    }

    /**
     * Helper method to convert an Instant object to a long instant.
     * @param instant the Instant object
     * @return the long instant
     */
    default Long convertToLong(Instant instant) {
        return instant.toEpochMilli();
    }
}
