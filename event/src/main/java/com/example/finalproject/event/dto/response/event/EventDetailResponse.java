package com.example.finalproject.event.dto.response.event;

import com.example.finalproject.event.model.EventCategories;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventDetailResponse {

    private String title;
    private String shortSummary;
    private String description;
    private String imageUrl;

    private LocalDate date;
    private LocalTime time;

    private EventCategories category;
    private Integer totalCapacity;

    private LocationResponse location;
    private List<TicketResponse> tickets;

    // tambahan khusus detail
    private Integer remainingCapacity;
    private String warningMessage;
}
