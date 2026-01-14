package com.example.finalproject.event.exception.Booking;

public class PaymentAmountNotEnoughException extends RuntimeException {
    public PaymentAmountNotEnoughException() {
        super("Payment amount is not enough");
    }
}

