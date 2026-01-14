package com.example.finalproject.event.exception.Booking;

public class NoBookingsFoundException extends RuntimeException {
    public NoBookingsFoundException() {
        super("No bookings found");
    }
}
