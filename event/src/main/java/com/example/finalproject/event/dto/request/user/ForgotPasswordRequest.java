package com.example.finalproject.event.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Please fill out this field")
    @Email(message = "Please enter a valid email address")
    private String email;
}
