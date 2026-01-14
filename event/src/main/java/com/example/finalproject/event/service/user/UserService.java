package com.example.finalproject.event.service.user;

import com.example.finalproject.event.config.SecurityUtil;
import com.example.finalproject.event.dto.request.user.PatchUserRequest;
import com.example.finalproject.event.dto.request.user.UpdateUserRequest;
import com.example.finalproject.event.dto.response.user.UserProfileResponse;
import com.example.finalproject.event.exception.user.UserNotFoundException;
import com.example.finalproject.event.model.user.UserModel;
import com.example.finalproject.event.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // ===== PUT =====
    public UserProfileResponse updateUser(UpdateUserRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return userMapper.getProfileResponse(user);
    }

    // ===== PATCH =====
    public UserProfileResponse patchUser(PatchUserRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();

        UserModel user = userRepository.findByEmail(email)
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

        userRepository.save(user);
        return userMapper.getProfileResponse(user);
    }

    // ===== GET MY PROFILE =====
    public UserProfileResponse getMyProfile(){
        String email = SecurityUtil.getCurrentUserEmail();

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return userMapper.getProfileResponse(user);
    }
}
