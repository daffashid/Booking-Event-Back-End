package com.example.finalproject.event.service.user;

import com.example.finalproject.event.dto.request.PatchUserRequest;
import com.example.finalproject.event.dto.request.UpdateUserRequest;
import com.example.finalproject.event.exception.user.UserNotFoundException;
import com.example.finalproject.event.model.UserModel;
import com.example.finalproject.event.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ===== PUT =====
    public UserModel updateUser(Long id, UpdateUserRequest request) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // ===== PATCH =====
    public UserModel patchUser(Long id, PatchUserRequest request) {
        UserModel user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        if (request.getFirstName() != null)
            user.setFirstName(request.getFirstName());

        if (request.getLastName() != null)
            user.setLastName(request.getLastName());

        if (request.getUserName() != null)
            user.setUserName(request.getUserName());

        if (request.getEmail() != null)
            user.setEmail(request.getEmail());

        if (request.getPhoneNumber() != null)
            user.setPhoneNumber(request.getPhoneNumber());

        if (request.getProfilePicture() != null)
            user.setProfilePicture(request.getProfilePicture());

        user.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }
}
