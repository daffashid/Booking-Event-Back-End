package com.example.finalproject.event.exception.Booking;

public class EventSoldOutException extends RuntimeException {
    public EventSoldOutException() {
        super("Tickets are sold out");
    }
}
