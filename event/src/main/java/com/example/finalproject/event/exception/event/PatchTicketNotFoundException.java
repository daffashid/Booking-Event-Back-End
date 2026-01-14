package com.example.finalproject.event.exception.event;

public class PatchTicketNotFoundException extends RuntimeException{
    public PatchTicketNotFoundException(Long ticketId) {
        super("Ticket not found with id: " + ticketId);
    }
}
