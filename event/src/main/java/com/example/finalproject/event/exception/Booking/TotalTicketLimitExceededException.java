package com.example.finalproject.event.exception.Booking;

public class TotalTicketLimitExceededException extends RuntimeException {
    public TotalTicketLimitExceededException() {
        super("Total ticket per booking cannot exceed 10");
    }
}
