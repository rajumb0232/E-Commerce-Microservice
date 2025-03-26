package com.example.user.application.service;

import com.example.user.application.repository.UserRepository;
import com.example.user.application.service.contracts.UserAccountManagementService;
import com.example.user.domain.exception.UserNotFoundByIdException;
import com.example.user.application.mapping.UserAndViewMapper;
import com.example.user.application.dto.UserRequest;
import com.example.user.application.dto.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAccountManagementServiceImpl implements UserAccountManagementService {

    private final UserRepository userRepository;
    private final UserAndViewMapper userAndViewMapper;

    @Override
    public UserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(userAndViewMapper::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to find user by ID: " + id));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.updateUser(request.username(), request.email());
                    user = userRepository.update(user);
                    return userAndViewMapper.mapToUserResponse(user);
                })
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to update user by ID: " + id));
    }

    @Override
    public void deleteUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to delete user by ID: " + id));

        user.deleteUser();
        userRepository.update(user);
    }
}
