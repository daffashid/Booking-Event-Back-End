package com.example.finalproject.event.dto.response.event;

import com.example.finalproject.event.model.event.EventCategories;
import com.example.finalproject.event.model.event.EventType;
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
    private String shortSummary;
    private String description;
    private String imageUrl;

    private LocalDate date;
    private LocalTime time;

    private EventCategories category;
    private EventType eventType;
    private Integer totalCapacity;

    private LocationResponse location;
    private OnlineEventResponse onlineEvent;
    private List<TicketResponse> tickets;
}
