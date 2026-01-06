package com.example.finalproject.event.exception.user;

public class UserNotFound extends RuntimeException{
    public UserNotFound() {
        super("User not found");
    }
}
