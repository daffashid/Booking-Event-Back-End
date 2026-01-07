package com.example.finalproject.event.dto.event;

import com.example.finalproject.event.model.EventCategories;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PatchEventRequest {
    private String title;
    private String shortSummary;
    private String description;
    private EventCategories category;
    private LocalDate date;
    private LocalTime time;
    private Integer totalCapacity;

    // Location
    private String venue;
    private String address;
    private String city;
    private String country;
}
