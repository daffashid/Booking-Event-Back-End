package com.example.finalproject.event.service.user;

import com.example.finalproject.event.dto.response.user.UserProfileResponse;
import com.example.finalproject.event.model.UserModel;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    /* =========================
       USER DETAIL RESPONSE
       ========================= */
    public UserProfileResponse getProfileResponse(UserModel user){
        return UserProfileResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userName(user.getUserName())
                .profilePicture(user.getProfilePicture())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

}
