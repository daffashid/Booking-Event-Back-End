package com.example.finalproject.event.dto.response.Booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class BookingDetailResponse {

    private Long bookingId;
    private String eventTitle;
    private List<BookingItemResponse> items;

    private BigDecimal totalPrice;
    private String status;

    // muncul hanya jika PAID
    private LocalDateTime paidAt;
    private String qrCode;

    private LocalDateTime createdAt;
}


