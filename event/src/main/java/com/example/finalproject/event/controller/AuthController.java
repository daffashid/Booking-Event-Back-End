package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.request.auth.LoginRequest;
import com.example.finalproject.event.dto.request.auth.RegisterRequest;
import com.example.finalproject.event.exception.user.*;
import com.example.finalproject.event.dto.response.BaseResponse;
import com.example.finalproject.event.dto.response.admin.PatchAdminResponse;
import com.example.finalproject.event.dto.response.admin.RegisterResponse;
import com.example.finalproject.event.service.user.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = new RegisterResponse();

        try {
            response.setData(authService.register(request));
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
            response.setData(authService.promoteToAdmin(id));
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletResponse httpResponse) {
        BaseResponse<Void> response = new BaseResponse<>();

        try {
            String token = authService.login(request);
            authService.setTokenCookie(httpResponse, token);

            response.setSuccess(true);
            response.setMessage("Login successful");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EmailNotFoundException e) {
            response.setMessage("Email not registered");
            response.setErrorCode("01");

        } catch (InvalidCredentialException e) {
            response.setMessage("Incorrect password");
            response.setErrorCode("02");

        } catch (UnauthorizedAccessException e) {
            response.setMessage("You are not authorized to access this page");
            response.setErrorCode("03");

        } catch (Exception e) {
            response.setMessage("Something went wrong");
            response.setErrorCode("99");
        }

        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        authService.logout(response);

        return ResponseEntity.ok("Logout berhasil");
    }

    @GetMapping("/debug")
    public Object debugAuth(Authentication auth) {
        if (auth == null) {
            return "Authentication is NULL";
        }

        return Map.of(
                "principal", auth.getPrincipal(),
                "authorities", auth.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList(),
                "authenticated", auth.isAuthenticated()
        );
    }
}
