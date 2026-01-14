package com.example.finalproject.event.exception.Booking;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException() {
        super("Booking not found");
    }
}
