package com.example.finalproject.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "This field is required")
    private String firstName;

    @NotBlank(message = "This field is required")
    private String lastName;

    @NotBlank(message = "This field is required")
    @Column(unique = true)
    private String userName;

    private String profilePicture;

    @NotBlank(message = "This field is required")
    @Email(message = "Please enter a valid email address")
    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @NotBlank(message = "This field is required")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role; // ROLE_ADMIN, ROLE_USER

    private String resetPasswordToken;
    private LocalDateTime resetTokenExpiredAt;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
