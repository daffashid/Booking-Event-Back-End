package com.example.finalproject.event.dto.event;

import com.example.finalproject.event.model.EventCategories;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class UpdateEventRequest {
    @NotBlank
    private String title;

    private String shortSummary;

    @NotBlank
    private String description;

    @NotNull
    private EventCategories category;

    private MultipartFile image;

    @Future
    private LocalDate date;

    private LocalTime time;

    @Min(1)
    private Integer totalCapacity;

    // Location
    @NotBlank
    private String venue;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String country;
}
