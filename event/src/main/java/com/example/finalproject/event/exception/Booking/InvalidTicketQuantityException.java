package com.example.finalproject.event.exception.Booking;

public class InvalidTicketQuantityException extends RuntimeException {
    public InvalidTicketQuantityException() {
        super("Ticket quantity must be between 1 and 10");
    }
}
