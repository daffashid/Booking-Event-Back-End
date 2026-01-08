package com.example.finalproject.event.dto.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    private String venue;
    private String city;
}
