package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.request.user.PatchUserRequest;
import com.example.finalproject.event.dto.request.user.UpdateUserRequest;
import com.example.finalproject.event.dto.response.user.UserProfileResponse;
import com.example.finalproject.event.dto.response.BaseResponse;
import com.example.finalproject.event.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/me")
    public ResponseEntity<BaseResponse<UserProfileResponse>> updateUser(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UserProfileResponse user = userService.updateUser(request);

        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "User updated successfully",
                        "00",
                        user
                )
        );
    }

    @PatchMapping("/me")
    public ResponseEntity<BaseResponse<UserProfileResponse>> patchUser(
            @RequestBody PatchUserRequest request
    ) {
        UserProfileResponse user = userService.patchUser(request);

        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "User updated partially",
                        "00",
                        user
                )
        );
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserProfileResponse>> getMyProfile(){
        UserProfileResponse response =
                userService.getMyProfile();

        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "Get user profile success",
                        "00",
                        response
                )
        );
    }
}

