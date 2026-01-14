package com.example.finalproject.event.exception.Booking;

public class InsufficientTicketStockException extends RuntimeException {

    public InsufficientTicketStockException() {
        super("Requested tickets exceed available quota");
    }
}
