package com.example.finalproject.event.dto.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnlineEventResponse {
    private String platform;
    private String linkUrl;
}
