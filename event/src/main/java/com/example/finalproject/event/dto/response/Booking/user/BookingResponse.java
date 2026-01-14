package com.example.finalproject.event.dto.response.Booking.user;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private String eventTitle;
    private List<BookingItemResponse> items;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
}
