package com.example.finalproject.event.dto.response.event;

import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventType;
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
    private EventType eventType;
    private EventCategories category;
    private LocalDate date;
    private LocalTime time;
    private LocationResponse location;        // OFFLINE
    private OnlineEventResponse onlineEvent;  // ONLINE
    private Integer price;
    private Integer totalCapacity;
    private Integer remainingCapacity;
}
