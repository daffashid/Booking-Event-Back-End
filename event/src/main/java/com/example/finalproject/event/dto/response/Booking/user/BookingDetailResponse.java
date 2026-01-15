package com.example.finalproject.event.dto.response.Booking.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
public class BookingDetailResponse {

    private Long bookingId;
    private String eventTitle;

    private LocalDate eventDate;
    private LocalTime eventTime;
    private String venue;     // offline
    private String meetingUrl; // online

    private List<BookingItemResponse> items;

    private BigDecimal totalPrice;
    private String status;

    private LocalDateTime paidAt;
    private String qrCode;

    private LocalDateTime createdAt;
}
