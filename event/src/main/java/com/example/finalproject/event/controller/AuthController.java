package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.AdminLoginRequest;
import com.example.finalproject.event.dto.RegisterRequest;
import com.example.finalproject.event.exception.user.*;
import com.example.finalproject.event.response.BaseResponse;
import com.example.finalproject.event.response.admin.PatchAdminResponse;
import com.example.finalproject.event.response.admin.RegisterResponse;
import com.example.finalproject.event.service.user.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AdminService adminService;

    public AuthController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = new RegisterResponse();

        try {
            response.setData(adminService.register(request));
            response.setMessage("success");
            response.setSuccess(true);
            response.setErrorCode("00");
            return ResponseEntity.ok(response);

        } catch (EmailAlreadyExistsException e) {
            response.setMessage("Email already exists");
            response.setErrorCode("02");

        } catch (PhoneNumberExistedException e) {
            response.setMessage("Phone number already exists");
            response.setErrorCode("03");

        } catch (PasswordMismatchException e) {
            response.setMessage("Passwords do not match");
            response.setErrorCode("04");

        } catch (WeakPasswordException e) {
            response.setMessage("Password must be at least 8 characters and contain both letters and numbers");
            response.setErrorCode("05");

        } catch (Exception e) {
            response.setMessage("Something went wrong");
            response.setErrorCode("99");
        }

        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }


    @PatchMapping("/{id}/role")
    public ResponseEntity<PatchAdminResponse> promoteUser(
            @PathVariable Long id
    ) {
        PatchAdminResponse response = new PatchAdminResponse();

        try {
            response.setData(adminService.promoteToAdmin(id));
            response.setSuccess(true);
            response.setMessage("User promoted to admin");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("User not found");
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Internal server error");
            response.setErrorCode("99");

            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequest request) {
        String token = adminService.login(request);

        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "Login successful",
                        "00",
                        token
                )
        );
    }
}
