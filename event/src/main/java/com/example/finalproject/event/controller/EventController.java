    package com.example.finalproject.event.controller;

    import com.example.finalproject.event.dto.CreateEventRequest;
    import com.example.finalproject.event.exception.event.CategoryEventNotFoundException;
    import com.example.finalproject.event.exception.event.EventNotFoundException;
    import com.example.finalproject.event.model.EventCategories;
    import com.example.finalproject.event.model.EventModel;
    import com.example.finalproject.event.response.BaseResponse;
    import com.example.finalproject.event.response.event.EventListItemResponse;
    import com.example.finalproject.event.response.event.EventResponse;
    import com.example.finalproject.event.response.event.LocationResponse;
    import com.example.finalproject.event.response.event.TicketResponse;
    import com.example.finalproject.event.service.EventService;
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
            EventModel event = eventService.createEvent(request);

            EventResponse eventResponse = new EventResponse(
                    event.getEventId(),
                    event.getTitle(),
                    event.getCategory(),
                    event.getTotalCapacity(),
                    new LocationResponse(
                            event.getLocation().getVenue(),
                            event.getLocation().getCity()
                    ),
                    event.getTickets().stream()
                            .map(t -> new TicketResponse(
                                    t.getTicketName(),
                                    t.getPrice(),
                                    t.getQuantity()
                            ))
                            .toList()
            );

            BaseResponse<EventResponse> response =
                    new BaseResponse<>(
                            true,
                            "Event created successfully",
                            "00",
                            eventResponse
                    );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }

        @GetMapping
        public ResponseEntity<BaseResponse<List<EventListItemResponse>>> getAllEvents() {

            List<EventListItemResponse> events = eventService.getAllEvents();

            return ResponseEntity.ok(
                    new BaseResponse<>(
                            true,
                            "Events fetched successfully",
                            "00",
                            events
                    )
            );
        }

        @GetMapping("/category/{category}")
        public ResponseEntity<BaseResponse<List<EventListItemResponse>>> getByCategory(
                @PathVariable EventCategories category
        ) {
            try {
                return ResponseEntity.ok(
                        new BaseResponse<>(
                                true,
                                "Events fetched successfully",
                                "00",
                                eventService.getEventsByCategory(category)
                        )
                );

            } catch (CategoryEventNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(
                                false,
                                "No events found for this category",
                                "01",
                                null
                        )
                );
            }
        }

        @GetMapping("/{id}")
        public ResponseEntity<BaseResponse<EventResponse>> getEventDetail(
                @PathVariable Long id
        ) {
            try {
                EventResponse event = eventService.getEventDetail(id);

                return ResponseEntity.ok(
                        new BaseResponse<>(
                                true,
                                "Event fetched successfully",
                                "00",
                                event
                        )
                );

            } catch (EventNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(
                                false,
                                "Event not found",
                                "01",
                                null
                        )
                );
            }
        }


        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BaseResponse<Void>> deleteEvent(
                @PathVariable Long id
        ) {
            try {
                eventService.deleteEvent(id);

                return ResponseEntity.ok(
                        new BaseResponse<>(
                                true,
                                "Event deleted successfully",
                                "00",
                                null
                        )
                );

            } catch (EventNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new BaseResponse<>(
                                false,
                                "Event not found",
                                "01",
                                null
                        )
                );

            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        new BaseResponse<>(
                                false,
                                "Internal server error",
                                "99",
                                null
                        )
                );
            }
        }
    }
