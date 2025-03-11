package com.example.user.service;

import com.example.user.exception.UserNotFoundByIdException;
import com.example.user.mapper.UserMapper;
import com.example.user.model.User;
import com.example.user.dto.request.RegistrationRequest;
import com.example.user.dto.request.UserRequest;
import com.example.user.dto.response.UserResponse;
import com.example.user.respository.UserRepository;
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
        User user = userMapper.mapToNewUser(request);
        encodeUserPassword(user);
        userRepository.save(user);
        return userMapper.mapToUserResponse(user);
    }

    /**
     * Encodes the user's password using the provided password encoder.
     * @param user the user to encode the password for
     */
    private void encodeUserPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
    }

    public UserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to find user by ID: " + id));
    }

    public UserResponse updateUser(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    userMapper.mapToUser(request, user);
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
