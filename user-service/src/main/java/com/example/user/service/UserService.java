package com.example.user.service;

import com.example.user.enums.UserRole;
import com.example.user.exception.UserNotFoundByIdException;
import com.example.user.mapper.UserMapper;
import com.example.user.model.User;
import com.example.user.requestdto.RegistrationRequest;
import com.example.user.responsedto.UserResponse;
import com.example.user.respository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse registerUser(RegistrationRequest request, UserRole role) {
        User user = userMapper.mapToNewUser(request);
        user.setRole(role);
        userRepository.save(user);
        return userMapper.mapToUserResponse(user);
    }

    public UserResponse findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::mapToUserResponse)
                .orElseThrow(() -> new UserNotFoundByIdException("Failed to find user by ID: " + id));
    }
}
