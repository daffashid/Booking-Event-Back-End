package com.example.finalproject.event.service.booking;

import com.example.finalproject.event.dto.response.Booking.BookingDetailResponse;
import com.example.finalproject.event.dto.response.Booking.BookingItemResponse;
import com.example.finalproject.event.dto.response.Booking.BookingResponse;
import com.example.finalproject.event.model.booking.BookingItemModel;
import com.example.finalproject.event.model.booking.BookingModel;
import com.example.finalproject.event.model.booking.BookingStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BookingMapper {

    public BookingResponse toResponse(BookingModel booking) {

        List<BookingItemResponse> items = booking.getItems().stream()
                .map(this::toItemResponse)
                .toList();

        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .eventTitle(booking.getEvent().getTitle())
                .items(items)
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .build();
    }

    private BookingItemResponse toItemResponse(BookingItemModel item) {

        BigDecimal subtotal =
                item.getPriceSnapshot()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));

        return BookingItemResponse.builder()
                .ticketId(item.getTicket().getTicketId())
                .ticketName(item.getTicket().getTicketName())
                .quantity(item.getQuantity())
                .price(item.getPriceSnapshot())
                .subtotal(subtotal)
                .build();
    }

    public BookingDetailResponse toDetailResponse(BookingModel booking) {

        boolean paid = booking.getStatus() == BookingStatus.PAID;

        return BookingDetailResponse.builder()
                .bookingId(booking.getBookingId())
                .eventTitle(booking.getEvent().getTitle())
                .items(
                        booking.getItems().stream()
                                .map(item -> BookingItemResponse.builder()
                                        .ticketId(item.getTicket().getTicketId())
                                        .ticketName(item.getTicket().getTicketName())
                                        .quantity(item.getQuantity())
                                        .price(item.getPriceSnapshot())
                                        .subtotal(
                                                item.getPriceSnapshot()
                                                        .multiply(BigDecimal.valueOf(item.getQuantity()))
                                        )
                                        .build())
                                .toList()
                )
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus().name())
                .paidAt(paid ? booking.getPaidAt() : null)
                .qrCode(paid ? booking.getQrCode() : null)
                .createdAt(booking.getCreatedAt())
                .build();
    }
}
