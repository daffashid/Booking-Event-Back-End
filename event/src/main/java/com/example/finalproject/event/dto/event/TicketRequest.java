package com.example.finalproject.event.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TicketRequest {

    @NotBlank(message = "Ticket name is required")
    private String ticketName;

    @Min(value = 0, message = "Ticket price must be positive")
    private Integer price;

    @Min(value = 1, message = "Ticket quantity must be at least 1")
    private Integer quantity;
}