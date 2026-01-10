package com.example.finalproject.event.dto.request.event;

import com.example.finalproject.event.model.EventCategories;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class CreateEventRequest {

    // ===== BASIC INFO =====
    @NotBlank(message = "This field is required")
    private String title;

    private String shortSummary;

    @NotBlank(message = "This field is required")
    private String description;

    @NotNull(message = "This field is required")
    private EventCategories category;

    @NotBlank(message = "This field is required")
    @org.hibernate.validator.constraints.URL(
            message = "Please enter a valid image URL"
    )
    private String imageUrl;

    // ===== DATE & TIME =====
    @Future(message = "Event date must be in the future")
    private LocalDate date;

    private LocalTime time;

    @NotNull(message = "This field is required")
    @Min(value = 1, message = "Quota must be greater than zero")
    private Integer totalCapacity;

    // ===== LOCATION =====
    @NotBlank(message = "This field is required")
    private String venue;

    @NotBlank(message = "This field is required")
    private String address;

    @NotBlank(message = "This field is required")
    private String city;

    @NotBlank(message = "This field is required")
    private String country;

    // ===== TICKETS =====
    @NotNull(message = "This field is required")
    private List<@Valid  TicketRequest> tickets;

    @Data
    public static class TicketRequest {
        @NotBlank(message = "This field is required")
        private String ticketName;

        @Min(value = 1, message = "Price must be greater than zero")
        private Integer price;

        @Min(value = 1, message = "Quantity must be greater than zero")
        private Integer quantity;
    }
}
