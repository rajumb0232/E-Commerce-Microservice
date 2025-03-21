package com.example.user.application;

import com.example.user.domain.exception.UserNotFoundByIdException;
import com.example.user.infrastructure.mapping.UserMapper;
import com.example.user.domain.model.User;
import com.example.user.application.dto.RegistrationRequest;
import com.example.user.application.dto.UserRequest;
import com.example.user.application.dto.UserResponse;
import com.example.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegistrationRequest request) {
        User user = User.create(request.username(), request.email(), encodeUserPassword(request.password()), request.role());
        userRepository.save(user);
        return userMapper.mapToUserResponse(user);
    }

    /**
     * Encodes the user's password using the provided password encoder.
     * @param rawPassword the raw password to be encoded
     * @return the encoded password
     */
    private String encodeUserPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public UserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to find user by ID: " + id));
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.updateUser(request.username(), request.email());
                    userRepository.save(user);
                    return userMapper.mapToUserResponse(user);
                })
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to update user by ID: " + id));
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to delete user by ID: " + id));
        userRepository.delete(user);
    }
}
