package com.example.finalproject.event.exception.Booking;

public class UnauthorizedBookingAccessException extends RuntimeException {
    public UnauthorizedBookingAccessException() {
        super("You are not allowed to access this booking");
    }
}
