    package com.example.finalproject.event.controller;

    import com.example.finalproject.event.dto.request.event.CreateEventRequest;
    import com.example.finalproject.event.dto.request.event.PatchEventRequest;
    import com.example.finalproject.event.dto.request.event.UpdateEventRequest;
    import com.example.finalproject.event.dto.response.event.*;
    import com.example.finalproject.event.exception.event.CategoryEventNotFoundException;
    import com.example.finalproject.event.exception.event.EventNotFoundException;
    import com.example.finalproject.event.exception.event.EventSearchNotFoundException;
    import com.example.finalproject.event.model.EventCategories;
    import com.example.finalproject.event.model.EventModel;
    import com.example.finalproject.event.dto.response.BaseResponse;
    import com.example.finalproject.event.service.event.EventService;
    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.access.prepost.PreAuthorize;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;

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
                    event.getTitle(),
                    event.getShortSummary(),
                    event.getDescription(),
                    event.getImageUrl(),
                    event.getDate(),
                    event.getTime(),
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

        @GetMapping("/category/{category}")
        public ResponseEntity<BaseResponse<List<EventListItemResponse>>> getByCategory(
                @PathVariable EventCategories category
        ) {

            BaseResponse<List<EventListItemResponse>> response = new BaseResponse<>();

            try {
                response.setData(eventService.getEventsByCategory(category));
                response.setMessage("Events fetched successfully");
                response.setSuccess(true);
                response.setErrorCode("00");

                return ResponseEntity.ok(response);

            } catch (CategoryEventNotFoundException e) {
                response.setMessage("No events found in this category");
                response.setErrorCode("01");

            } catch (Exception e) {
                response.setMessage("Failed to load events. Please try again");
                response.setErrorCode("99");
            }

            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
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

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BaseResponse<EventResponse>> updateEvent(
                @PathVariable Long id,
                @Valid @RequestBody UpdateEventRequest request
        ) {
            BaseResponse<EventResponse> response = new BaseResponse<>();

            try {
                response.setData(eventService.updateEvent(id, request));
                response.setMessage("Event updated successfully");
                response.setSuccess(true);
                response.setErrorCode("00");

                return ResponseEntity.ok(response);

            } catch (EventNotFoundException e) {
                response.setMessage("Event not found");
                response.setErrorCode("01");

            } catch (IllegalArgumentException e) {
                response.setMessage("Invalid event data");
                response.setErrorCode("02");

            } catch (Exception e) {
                response.setMessage("Failed to update event");
                response.setErrorCode("99");
            }

            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
        }

        @PatchMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BaseResponse<EventResponse>> patchEvent(
                @PathVariable Long id,
                @Valid @RequestBody PatchEventRequest request
                ){
            BaseResponse<EventResponse> response = new BaseResponse<>();

            try {
                response.setData(eventService.patchEvent(id, request));
                response.setMessage("Event updated successfully");
                response.setSuccess(true);
                response.setErrorCode("00");

                return ResponseEntity.ok(response);

            } catch (EventNotFoundException e) {
                response.setMessage("Event not found");
                response.setErrorCode("01");

            } catch (IllegalArgumentException e) {
                response.setMessage("Invalid event data");
                response.setErrorCode("02");

            } catch (Exception e) {
                response.setMessage("Failed to update event");
                response.setErrorCode("99");
            }

            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
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
