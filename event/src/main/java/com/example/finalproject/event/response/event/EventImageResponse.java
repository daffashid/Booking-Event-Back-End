package com.example.finalproject.event.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EventImageResponse {
    private Long eventId;
    private String imageUrl;
}
