package com.example.finalproject.event.exception.event;

public class InvalidTicketEventException extends RuntimeException {

    public InvalidTicketEventException(Long ticketId, Long eventId) {
        super("Ticket with id " + ticketId + " does not belong to event " + eventId);
    }
}
