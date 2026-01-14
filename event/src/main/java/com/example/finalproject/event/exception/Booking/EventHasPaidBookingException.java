package com.example.finalproject.event.exception.Booking;

public class EventHasPaidBookingException extends RuntimeException {

    public EventHasPaidBookingException() {
        super("Event cannot be deleted because it has paid bookings");
    }
}