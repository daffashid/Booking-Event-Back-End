package com.example.finalproject.event.service.user;

import com.example.finalproject.event.config.SecurityUtil;
import com.example.finalproject.event.dto.request.user.PatchUserRequest;
import com.example.finalproject.event.dto.request.user.UpdateUserRequest;
import com.example.finalproject.event.exception.user.UnauthorizedAccessException;
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

        String loggedInEmail = SecurityUtil.getCurrentUserEmail();

        if (!user.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedAccessException();
        }

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

        String loggedInEmail = SecurityUtil.getCurrentUserEmail();

        if (!user.getEmail().equals(loggedInEmail)) {
            throw new UnauthorizedAccessException();
        }

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
