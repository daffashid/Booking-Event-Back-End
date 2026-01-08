package com.example.finalproject.event.dto.response.event;

import com.example.finalproject.event.model.EventCategories;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EventListItemResponse {
    private Long eventId;
    private String title;
    private EventCategories category;
    private LocalDate date;
    private String venue;
    private String city;
    private Integer lowestPrice;
}
