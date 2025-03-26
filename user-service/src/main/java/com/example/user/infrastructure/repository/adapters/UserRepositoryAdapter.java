package com.example.user.infrastructure.repository.adapters;

import com.example.user.application.repository.UserRepository;
import com.example.user.domain.exception.UserNotFoundByIdException;
import com.example.user.domain.model.User;
import com.example.user.infrastructure.mapping.UserMapper;
import com.example.user.infrastructure.repository.UserEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserEntityRepository userEntityRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> findById(Long id) {
        return userEntityRepository.findById(id)
                .map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        var userEntity = userMapper.toEntity(user);
        userEntity = userEntityRepository.save(userEntity);

        return userMapper.toDomain(userEntity);
    }

    @Override
    public User update(User user) {
        return userEntityRepository.findById(user.getUserId())
                .map(userEntity -> {
                    userMapper.toEntity(user, userEntity);
                    userEntityRepository.save(userEntity);
                    return userMapper.toDomain(userEntity);
                })
                .orElseThrow(() -> new UserNotFoundByIdException("No user found with id: " + user.getUserId()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userEntityRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
}
