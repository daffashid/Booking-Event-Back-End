package com.example.finalproject.event.exception.Booking;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException() {
        super("Ticket not found");
    }
}
