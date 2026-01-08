package com.example.finalproject.event.dto.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventImageResponse {
    private Long eventId;
    private String imageUrl;
}
