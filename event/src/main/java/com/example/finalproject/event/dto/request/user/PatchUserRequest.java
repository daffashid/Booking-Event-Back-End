package com.example.finalproject.event.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class PatchUserRequest {

    private String firstName;
    private String lastName;
    private String userName;

    @Email
    private String email;

    private String phoneNumber;

    private String profilePicture;
}
