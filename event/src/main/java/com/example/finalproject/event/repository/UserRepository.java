package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.user.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByPhoneNumber(String phoneNumber);
    Optional<UserModel> findByUserName(String userName);
    Optional<UserModel> findByResetPasswordToken(String token);
    boolean existsByEmail(String email);
}
