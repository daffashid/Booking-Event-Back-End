package com.example.finalproject.event.model;

public enum EventCategories {
    SPORTS("Sports"),
    MUSIC("Music"),
    ARTS_THEATRE("Arts & Theatre"),
    FESTIVALS("Festivals"),
    FAMILY("Family"),
    COMEDY("Comedy");

    private final String displayValue;

    EventCategories(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
