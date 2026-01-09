package com.example.finalproject.event.exception.event;

public class NoActiveEventException extends RuntimeException{
    public NoActiveEventException(){
        super("No Event Available");
    }
}
