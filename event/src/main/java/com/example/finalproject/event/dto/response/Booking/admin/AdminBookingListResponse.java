package com.example.finalproject.event.dto.response.Booking.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class AdminBookingListResponse {
    private Long bookingId;
    private String eventTitle;
    private String userEmail;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime createdAt;
}
