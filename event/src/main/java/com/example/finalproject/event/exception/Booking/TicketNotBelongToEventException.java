package com.example.finalproject.event.exception.Booking;

public class TicketNotBelongToEventException extends RuntimeException {

    public TicketNotBelongToEventException() {
        super("Ticket does not belong to this event");
    }
}
