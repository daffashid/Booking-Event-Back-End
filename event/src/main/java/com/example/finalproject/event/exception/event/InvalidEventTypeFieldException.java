package com.example.finalproject.event.exception.event;

public class InvalidEventTypeFieldException extends RuntimeException {
    public InvalidEventTypeFieldException() {
        super("Invalid fields for this event type");
    }
}
