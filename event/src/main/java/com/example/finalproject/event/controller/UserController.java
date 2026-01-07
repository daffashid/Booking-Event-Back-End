package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.PatchUserRequest;
import com.example.finalproject.event.dto.UpdateUserRequest;
import com.example.finalproject.event.model.UserModel;
import com.example.finalproject.event.response.BaseResponse;
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

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserModel>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UserModel user = userService.updateUser(id, request);

        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "User updated successfully",
                        "00",
                        user
                )
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<UserModel>> patchUser(
            @PathVariable Long id,
            @RequestBody PatchUserRequest request
    ) {
        UserModel user = userService.patchUser(id, request);

        return ResponseEntity.ok(
                new BaseResponse<>(
                        true,
                        "User updated partially",
                        "00",
                        user
                )
        );
    }
}

