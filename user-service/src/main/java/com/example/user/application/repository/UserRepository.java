package com.example.user.application.repository;

import com.example.user.domain.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    User save(User user);
    User update(User user);
    Optional<User> findByEmail(String email);
}
