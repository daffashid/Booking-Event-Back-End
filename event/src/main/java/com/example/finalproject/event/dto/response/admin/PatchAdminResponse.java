package com.example.finalproject.event.dto.response.admin;

import com.example.finalproject.event.dto.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class PatchAdminResponse extends BaseResponse {
    private updateRole data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateRole{
        private Long userId;
        private String username;
        private String role;
    }
}
