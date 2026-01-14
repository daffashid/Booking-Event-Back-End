package com.example.finalproject.event.dto.request.event;

import com.example.finalproject.event.model.event.EventCategories;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class PatchEventRequest {
    private String title;
    private String shortSummary;
    private String description;
    private EventCategories category;
    private String imageUrl;
    private LocalDate date;
    private LocalTime time;
    private Integer totalCapacity;

    // OFFLINE
    private String venue;
    private String address;
    private String city;
    private String country;

    // ONLINE
    private String platform;
    private String linkUrl;

    private List<@Valid PatchTicketRequest> tickets;

    @Data
    public static class PatchTicketRequest {
        private String ticketName;

        @Min(value = 1, message = "Price must be greater than zero")
        private Integer price;

        @Min(value = 0, message = "Quantity must be greater than zero")
        private Integer quantity;

        private Boolean delete;
    }
}
