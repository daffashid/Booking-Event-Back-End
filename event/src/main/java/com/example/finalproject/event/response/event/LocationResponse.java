package com.example.finalproject.event.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationResponse {
    private String venue;
    private String city;
}
