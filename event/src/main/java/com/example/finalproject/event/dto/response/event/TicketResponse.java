package com.example.finalproject.event.dto.response.event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class TicketResponse {
    private String ticketName;
    private BigDecimal price;
    private Integer quantity;
}
