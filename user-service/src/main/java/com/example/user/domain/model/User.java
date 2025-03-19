package com.example.user.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    @CreatedDate
    private LocalDate createdAt;
    @LastModifiedDate
    private LocalDate updatedAt;
    private LocalDate deletedAt;
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
        this.deletedAt = LocalDate.now();
    }


}
