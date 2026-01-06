package com.example.finalproject.event.exception.event;

public class CategoryEventNotFoundException extends RuntimeException {
    public CategoryEventNotFoundException() {
        super("No events found for this category");
    }
}
