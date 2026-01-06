package com.example.finalproject.event.response.admin;

import com.example.finalproject.event.response.BaseResponse;
import lombok.Data;

@Data
public class RegisterResponse extends BaseResponse<String> {
    private String data;
}
