package com.example.finalproject.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class TicketModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    private String ticketName;

    @Min(0)
    @Column(nullable = false)
    private BigDecimal price;

    @Min(0)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private EventModel event;
}
