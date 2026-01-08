package com.example.finalproject.event.dto.request.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "This field is required")
    private String firstName;

    @NotBlank(message = "This field is required")
    private String lastName;

    @NotBlank(message = "This field is required")
    private String userName;

    @NotBlank(message = "This field is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "This field is required")
    @Pattern(
            regexp = "^[0-9]{10,}$",
            message = "Phone number must contain numbers only"
    )
    private String phoneNumber;

    @NotBlank(message = "This field is required")
    private String password;

    @NotBlank(message = "This field is required")
    private String confirmPassword;
}
