package com.example.finalproject.event.exception.event;

public class InvalidTimeException extends EventException {
    public InvalidTimeException() {
        super("Invalid event time");
    }
}
