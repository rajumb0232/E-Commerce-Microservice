package com.example.user.service;

import com.example.user.enums.UserRole;
import com.example.user.exception.UserNotFoundByIdException;
import com.example.user.mapper.UserMapper;
import com.example.user.model.User;
import com.example.user.requestdto.RegistrationRequest;
import com.example.user.requestdto.UserRequest;
import com.example.user.responsedto.UserResponse;
import com.example.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse registerUser(RegistrationRequest request) {
        User user = userMapper.mapToNewUser(request);
        userRepository.save(user);
        return userMapper.mapToUserResponse(user);
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
