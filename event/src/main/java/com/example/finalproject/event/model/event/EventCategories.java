package com.example.finalproject.event.model.event;

public enum EventCategories {
    SPORTS ("Sports"),
    MUSIC("Music"),
    ARTS_THEATRE("Arts & Theatre"),
    FESTIVALS("Festivals"),
    FAMILY("Family"),
    COMEDY("Comedy"),
    CONFERENCE("Conference"),
    WORKSHOP("Workshop"),
    EXHIBITION("Exhibition"),
    SEMINAR("Seminar"),
    COMPETITION("Competition");

    private final String displayValue;

    EventCategories(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
