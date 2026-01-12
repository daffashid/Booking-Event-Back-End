package com.example.finalproject.event.dto.response.event;

import com.example.finalproject.event.model.EventCategories;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class EventListItemResponse {
    private Long eventId;
    private String title;
    private String shortSummary;
    private String imageUrl;
    private EventCategories category;
    private LocalDate date;
    private LocalTime time;
    private String venue;
    private String city;
    private Integer price;
    private Integer totalCapacity;
    private Integer remainingCapacity;
}
