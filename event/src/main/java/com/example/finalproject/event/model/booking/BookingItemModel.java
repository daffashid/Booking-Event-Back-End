package com.example.finalproject.event.model.booking;

import com.example.finalproject.event.model.event.TicketModel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookingItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookingModel booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private TicketModel ticket;

    private Integer quantity;

    // harga tiket saat booking (ANTI price manipulation)
    private BigDecimal priceSnapshot;
}
