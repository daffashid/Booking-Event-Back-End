package com.example.finalproject.event.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @NotBlank(message = "This field is required")
    private String title;

    private String shortSummary;

    @NotBlank(message = "This field is required")
    private String description;

    @Enumerated(EnumType.STRING)
    private EventCategories category;

    private String imageUrl;

    @Future(message = "Event date must be in the future")
    private LocalDate date;

    private LocalTime time;

    @Min(value = 0)
    private Integer totalCapacity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationModel location;

    @OneToMany(
            mappedBy = "event",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TicketModel> tickets;

}
