package com.example.finalproject.event.dto.request.event;

import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class UpdateEventRequest {

    // ===== BASIC =====
    @NotBlank
    private String title;

    private String shortSummary;

    @NotBlank
    private String description;

    @NotNull
    private EventCategories category;

    @NotBlank
    @URL
    private String imageUrl;

    @NotNull
    @Future
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    @Min(1)
    private Integer totalCapacity;

    // ===== OFFLINE =====
    private String venue;
    private String address;
    private String city;
    private String country;

    // ===== ONLINE =====
    private String platform;
    private String linkUrl;

    // ===== TICKETS =====
    @NotNull
    private List<@Valid TicketRequest> tickets;

    @Data
    public static class TicketRequest {
        @NotBlank
        private String ticketName;

        @Min(1)
        private Integer price;

        @Min(1)
        private Integer quantity;
    }
}
