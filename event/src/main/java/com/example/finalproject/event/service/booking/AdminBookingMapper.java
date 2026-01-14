package com.example.finalproject.event.service.booking;

import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingDetailResponse;
import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingItemResponse;
import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingListResponse;
import com.example.finalproject.event.model.booking.BookingModel;
import com.example.finalproject.event.model.booking.BookingStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AdminBookingMapper {

    public AdminBookingListResponse toListResponse(BookingModel booking) {
        return AdminBookingListResponse.builder()
                .bookingId(booking.getBookingId())
                .eventTitle(booking.getEvent().getTitle())
                .userEmail(booking.getUser().getEmail())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    public AdminBookingDetailResponse toDetailResponse(BookingModel booking) {

        boolean paid = booking.getStatus() == BookingStatus.PAID;

        return AdminBookingDetailResponse.builder()
                .bookingId(booking.getBookingId())
                .eventTitle(booking.getEvent().getTitle())
                .userEmail(booking.getUser().getEmail())
                .items(
                        booking.getItems().stream().map(item ->
                                AdminBookingItemResponse.builder()
                                        .ticketName(item.getTicket().getTicketName())
                                        .quantity(item.getQuantity())
                                        .price(item.getPriceSnapshot())
                                        .subtotal(
                                                item.getPriceSnapshot()
                                                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                                        )
                                        .build()
                        ).toList()
                )
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .paidAt(paid ? booking.getPaidAt() : null)
                .qrCode(paid ? booking.getQrCode() : null)
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
