package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.request.event.CreateEventRequest;
import com.example.finalproject.event.dto.request.event.PatchEventRequest;
import com.example.finalproject.event.dto.request.event.UpdateEventRequest;
import com.example.finalproject.event.dto.response.event.*;
import com.example.finalproject.event.exception.Booking.EventHasPaidBookingException;
import com.example.finalproject.event.exception.event.CategoryEventNotFoundException;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.exception.event.EventSearchNotFoundException;
import com.example.finalproject.event.exception.event.InvalidEventTypeFieldException;
import com.example.finalproject.event.model.event.EventCategories;
import com.example.finalproject.event.dto.response.BaseResponse;
import com.example.finalproject.event.service.event.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<EventResponse>> createEvent(
            @Valid @RequestBody CreateEventRequest request
    ) {
        BaseResponse<EventResponse> response = new BaseResponse<>();

        try {
            EventResponse eventResponse = eventService.createEvent(request);

            response.setData(eventResponse);
            response.setSuccess(true);
            response.setMessage("Event created successfully");
            response.setErrorCode("00");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to create event");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<EventListItemResponse>>> getAllEvents() {

        BaseResponse<List<EventListItemResponse>> response =
                new BaseResponse<>();

        try {
            response.setData(eventService.getAllEvents());
            response.setMessage("Events fetched successfully");
            response.setSuccess(true);
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventNotFoundException e) {
            response.setMessage("No events available");
            response.setErrorCode("01");

        } catch (Exception e) {
            response.setMessage("Failed to load events. Please try again");
            response.setErrorCode("99");
        }

        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/category")
    public ResponseEntity<BaseResponse<List<EventListItemResponse>>> getByCategory(
            @RequestParam String category
    ) {
        BaseResponse<List<EventListItemResponse>> response = new BaseResponse<>();

        try {
            EventCategories eventCategory =
                    EventCategories.valueOf(category.toUpperCase());

            response.setData(eventService.getEventsByCategory(eventCategory));
            response.setMessage("Events fetched successfully");
            response.setSuccess(true);
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage("Invalid category value");
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (CategoryEventNotFoundException e) {
            response.setSuccess(false);
            response.setMessage("No events found in this category");
            response.setErrorCode("01");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to load events. Please try again");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<EventDetailResponse>> getEventDetail(
            @PathVariable Long id
    ) {
        BaseResponse<EventDetailResponse> response =
                new BaseResponse<>();

        try {
            response.setData(eventService.getEventDetail(id));
            response.setMessage("Event fetched successfully");
            response.setSuccess(true);
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventNotFoundException e) {
            response.setMessage("Event not found");
            response.setErrorCode("01");

        } catch (Exception e) {
            response.setMessage("Failed to load event. Please try again");
            response.setErrorCode("99");
        }

        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<Void>> deleteEvent(@PathVariable Long id) {

        BaseResponse<Void> response = new BaseResponse<>();

        try {
            eventService.deleteEvent(id);

            response.setSuccess(true);
            response.setMessage("Event deleted successfully");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventHasPaidBookingException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("03");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (EventNotFoundException e) {

            response.setSuccess(false);
            response.setMessage("Event not found");
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage("Failed to delete event");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<EventResponse>> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEventRequest request
    ) {
        BaseResponse<EventResponse> response = new BaseResponse<>();

        try {
            response.setData(eventService.updateEvent(id, request));
            response.setSuccess(true);
            response.setMessage("Event updated successfully");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventNotFoundException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage()); // dari exception
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (InvalidEventTypeFieldException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage()); // dari exception
            response.setErrorCode("03");

            return ResponseEntity.badRequest().body(response);

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to update event");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BaseResponse<EventResponse>> patchEvent(
            @PathVariable Long id,
            @Valid @RequestBody PatchEventRequest request
    ) {
        BaseResponse<EventResponse> response = new BaseResponse<>();

        try {
            response.setData(eventService.patchEvent(id, request));
            response.setSuccess(true);
            response.setMessage("Event updated successfully");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventNotFoundException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage()); // dari exception
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (InvalidEventTypeFieldException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage()); // dari exception
            response.setErrorCode("03");

            return ResponseEntity.badRequest().body(response);

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to update event");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<BaseResponse<List<EventListItemResponse>>> searchEvents(
            @RequestParam String keyword
    ) {
        BaseResponse<List<EventListItemResponse>> response = new BaseResponse<>();

        try {
            List<EventListItemResponse> result =
                    eventService.searchEvents(keyword);

            response.setData(result);
            response.setMessage("Events fetched successfully");
            response.setSuccess(true);
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventSearchNotFoundException e) {
            response.setMessage("No events match your search");
            response.setErrorCode("01");

        } catch (IllegalArgumentException e) {
            response.setMessage("Invalid search keyword");
            response.setErrorCode("02");

        } catch (Exception e) {
            response.setMessage("Failed to search events");
            response.setErrorCode("99");
        }

        response.setSuccess(false);
        return ResponseEntity.badRequest().body(response);
    }

}
