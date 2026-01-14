package com.example.finalproject.event.dto.response.Booking.user;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BookingItemResponse {
    private Long ticketId;
    private String ticketName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
