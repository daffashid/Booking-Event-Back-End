package com.example.finalproject.event.dto.response.Booking.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AdminBookingItemResponse {

    private String ticketName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
