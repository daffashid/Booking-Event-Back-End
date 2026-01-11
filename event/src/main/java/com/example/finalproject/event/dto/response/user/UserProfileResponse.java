package com.example.finalproject.event.dto.response.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {
    private String firstName;
    private String lastName;
    private String userName;
    private String profilePicture;
    private String email;
    private String phoneNumber;
}
