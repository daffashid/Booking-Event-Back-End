package com.example.finalproject.event.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "please fill out this field")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "please fill out this field")
    private String password;
}
