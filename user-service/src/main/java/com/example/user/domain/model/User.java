package com.example.user.domain.model;

import lombok.*;

import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long userId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
    private boolean isDeleted;

    public static User create(String username, String email, String password, UserRole role) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.role = role;
        return user;
    }

    public void updateUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public void resetPassword(String password) {
        this.password = password;
    }

    public void deleteUser() {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }

    public void recoverDeleted() {
        this.isDeleted = false;
        this.deletedAt = null;
    }

}
