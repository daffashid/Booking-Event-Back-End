package com.example.finalproject.event.response.event;

import com.example.finalproject.event.model.EventCategories;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class EventResponse{
    private Long eventId;
    private String title;
    private EventCategories category;
    private LocalDate date;
    private LocalTime time;
    private String imageUrl;
    private Integer totalCapacity;
    private LocationResponse location;
    private List<TicketResponse> tickets;
}
