    package com.example.finalproject.event.controller;

    import com.example.finalproject.event.dto.event.CreateEventRequest;
    import com.example.finalproject.event.dto.event.PatchEventRequest;
    import com.example.finalproject.event.dto.event.UpdateEventRequest;
    import com.example.finalproject.event.exception.event.*;
    import com.example.finalproject.event.model.EventCategories;
    import com.example.finalproject.event.model.EventModel;
    import com.example.finalproject.event.response.BaseResponse;
    import com.example.finalproject.event.response.event.*;
    import com.example.finalproject.event.service.event.EventService;
    import jakarta.validation.Valid;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
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

        @PostMapping(
                value = "/create",
                consumes = MediaType.MULTIPART_FORM_DATA_VALUE
        )
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BaseResponse<Object>> createEvent(
                @Valid @ModelAttribute CreateEventRequest request,
                @RequestPart(value = "image", required = false) MultipartFile image
        ) {
            BaseResponse<Object> response = new BaseResponse<>();

            try {
                response.setData(eventService.createEvent(request, image));
                response.setSuccess(true);
                response.setMessage("Event created successfully");
                response.setErrorCode("00");
                return ResponseEntity.ok(response);

            } catch (InvalidDateException e) {
                response.setMessage(e.getMessage());
                response.setErrorCode("01");

            } catch (InvalidTimeException e) {
                response.setMessage(e.getMessage());
                response.setErrorCode("02");

            } catch (InvalidTicketException e) {
                response.setMessage(e.getMessage());
                response.setErrorCode("03");

            } catch (ImageUploadException e) {
                response.setMessage(e.getMessage());
                response.setErrorCode("04");

            } catch (Exception e) {
                response.setMessage("Something went wrong");
                response.setErrorCode("99");
            }

            response.setSuccess(false);
            return ResponseEntity.badRequest().body(response);
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

        @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BaseResponse<EventResponse>> updateEvent(
                @PathVariable Long id,
                @Valid @ModelAttribute UpdateEventRequest request
        ) {
            return ResponseEntity.ok(
                    new BaseResponse<>(
                            true,
                            "Event updated successfully",
                            "00",
                            eventService.updateEvent(id, request)
                    )
            );
        }

        @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<BaseResponse<EventResponse>> patchEvent(
                @PathVariable Long id,
                @ModelAttribute PatchEventRequest request
        ) {
            return ResponseEntity.ok(
                    new BaseResponse<>(
                            true,
                            "Event patched successfully",
                            "00",
                            eventService.patchEvent(id, request)
                    )
            );
        }
    }
