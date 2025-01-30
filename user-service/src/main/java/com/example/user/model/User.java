package com.example.user.model;

import com.example.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private LocalDate deletedAt;
    private boolean isDeleted;
}
