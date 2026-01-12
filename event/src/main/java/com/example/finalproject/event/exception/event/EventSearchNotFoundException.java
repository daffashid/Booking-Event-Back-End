package com.example.finalproject.event.exception.event;

public class EventSearchNotFoundException extends RuntimeException{
    public EventSearchNotFoundException() {
        super("No events found for the given keyword");
    }

    public EventSearchNotFoundException(String message) {
        super(message);
    }
}
