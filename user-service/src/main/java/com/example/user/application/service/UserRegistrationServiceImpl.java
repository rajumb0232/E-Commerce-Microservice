package com.example.user.application.service;

import com.example.user.application.service.contracts.UserRegistrationService;
import com.example.user.application.dto.RegistrationRequest;
import com.example.user.application.dto.UserResponse;
import com.example.user.application.mapping.UserMapper;
import com.example.user.domain.model.User;
import com.example.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     * @param request the registration request containing username, email, role and password
     * @return the registered user's response
     */
    public UserResponse register(RegistrationRequest request) {
        // Creating new user
        User user = User.create(
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.role());

        userRepository.save(user);
        return userMapper.mapToUserResponse(user);
    }
}
