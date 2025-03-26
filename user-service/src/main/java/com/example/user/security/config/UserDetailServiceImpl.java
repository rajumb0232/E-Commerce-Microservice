package com.example.user.security.config;

import com.example.user.application.repository.UserRepository;
import com.example.user.domain.model.User;
import com.example.user.infrastructure.mapping.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Loads the user's details by their username.
     * This method is transactional and read-only.
     *
     * @param username The username of the user to be loaded.
     * @return A UserDetails object containing the user's authentication information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(user -> {
                    log.info("Successful login for email: {}", username);
                    return createUser(user);
                })
                .orElseThrow(() -> {
                    log.error("Failed login attempt for email: {}", username);
                    return new UsernameNotFoundException("User not found with email: " + username);
                });
    }

    /**
     * Converts a User entity into a UserDetails object.
     *
     * @param user The User entity to be converted.
     * @return A UserDetails object with the user's username, password, and roles.
     */
    private UserDetails createUser(User user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
