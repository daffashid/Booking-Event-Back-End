package com.example.finalproject.event.dto.request.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class CreateBookingRequest {

    @NotNull
    private Long eventId;

    @NotNull
    @Size(min = 1, message = "At least one ticket must be selected")
    private List<@Valid TicketOrderRequest> tickets;

    @Data
    public static class TicketOrderRequest {

        @NotNull
        private Long ticketId;

        @NotNull
        private Integer quantity;
    }
}
