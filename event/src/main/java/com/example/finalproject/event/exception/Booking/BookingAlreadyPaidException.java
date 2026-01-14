package com.example.finalproject.event.exception.Booking;

public class BookingAlreadyPaidException extends RuntimeException {
    public BookingAlreadyPaidException() {
        super("Booking already paid");
    }
}

