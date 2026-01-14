package com.example.finalproject.event.exception.Booking;

public class InvalidBookingStatusException extends RuntimeException {
    public InvalidBookingStatusException() {
        super("Booking cannot be cancelled");
    }
}
