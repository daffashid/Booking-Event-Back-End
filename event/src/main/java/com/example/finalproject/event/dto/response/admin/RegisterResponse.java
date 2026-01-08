package com.example.finalproject.event.dto.response.admin;

import com.example.finalproject.event.dto.response.BaseResponse;
import lombok.Data;

@Data
public class RegisterResponse extends BaseResponse<String> {
    private String data;
}
