package com.example.finalproject.event.dto.event;

import com.example.finalproject.event.model.EventCategories;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateEventRequest {

    // ===== BASIC INFO =====
    @NotBlank(message = "Title is required")
    private String title;

    private String shortSummary;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Category is required")
    private EventCategories category;

    // ===== DATE & TIME (STRING FIRST) =====
    @NotBlank(message = "Date is required (yyyy-MM-dd)")
    private String date;

    @NotBlank(message = "Time is required (HH:mm)")
    private String time;

    // ===== CAPACITY =====
    @NotNull(message = "Total capacity is required")
    @Min(value = 1, message = "Total capacity must be greater than 0")
    private Integer totalCapacity;

    // ===== LOCATION =====
    @NotBlank(message = "Venue is required")
    private String venue;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    // ===== TICKETS (JSON STRING) =====
    @NotEmpty
    private List<TicketRequest> tickets;
}
