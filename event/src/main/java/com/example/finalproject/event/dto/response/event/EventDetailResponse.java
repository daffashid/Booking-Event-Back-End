package com.example.finalproject.event.dto.response.event;

import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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
    private EventType eventType;
    private EventCategories category;
    private Integer totalCapacity;
    // OFFLINE
    private LocationResponse location;
    // ONLINE
    private OnlineEventResponse onlineEvent;
    private List<TicketResponse> tickets;

    // tambahan khusus detail
    private Integer remainingCapacity;
    private String warningMessage;
}
