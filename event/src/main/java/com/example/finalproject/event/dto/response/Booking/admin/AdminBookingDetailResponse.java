package com.example.finalproject.event.dto.response.Booking.admin;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class AdminBookingDetailResponse {

    private Long bookingId;
    private String eventTitle;
    private String userEmail;

    private List<AdminBookingItemResponse> items;

    private BigDecimal totalPrice;
    private String status;

    private LocalDateTime paidAt;
    private String qrCode; // hanya muncul kalau PAID

    private LocalDateTime createdAt;
}
