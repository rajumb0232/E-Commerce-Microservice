package com.example.user.application.service;

import com.example.user.application.service.contracts.UserAccountManagementService;
import com.example.user.domain.exception.UserNotFoundByIdException;
import com.example.user.application.mapping.UserMapper;
import com.example.user.domain.model.User;
import com.example.user.application.dto.UserRequest;
import com.example.user.application.dto.UserResponse;
import com.example.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAccountManagementServiceImpl implements UserAccountManagementService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to find user by ID: " + id));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.updateUser(request.username(), request.email());
                    userRepository.save(user);
                    return userMapper.mapToUserResponse(user);
                })
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to update user by ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to delete user by ID: " + id));
        userRepository.delete(user);
    }
}
