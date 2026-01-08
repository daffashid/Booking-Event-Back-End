package com.example.finalproject.event.service.event;

import com.example.finalproject.event.dto.request.event.CreateEventRequest;
import com.example.finalproject.event.dto.request.event.PatchEventRequest;
import com.example.finalproject.event.dto.request.event.UpdateEventRequest;
import com.example.finalproject.event.dto.response.event.*;
import com.example.finalproject.event.exception.event.CategoryEventNotFoundException;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventModel;
import com.example.finalproject.event.model.LocationModel;
import com.example.finalproject.event.model.TicketModel;
import com.example.finalproject.event.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventService {

    private final ImageService imageService;
    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository,
                        ImageService imageService) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
    }

    public EventModel createEvent(CreateEventRequest request) {

        LocationModel location = LocationModel.builder()
                .venue(request.getVenue())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .build();

        EventModel event = EventModel.builder()
                .title(request.getTitle())
                .shortSummary(request.getShortSummary())
                .description(request.getDescription())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .date(request.getDate())
                .time(request.getTime())
                .totalCapacity(request.getTotalCapacity())
                .location(location)
                .build();

        if (request.getTickets() != null && !request.getTickets().isEmpty()) {

            int totalTicketQuantity = request.getTickets().stream()
                    .mapToInt(t -> t.getQuantity())
                    .sum();

            if (totalTicketQuantity != request.getTotalCapacity()) {
                throw new IllegalArgumentException(
                        "Total ticket quantity must equal totalCapacity"
                );
            }
            List<TicketModel> tickets = request.getTickets().stream()
                    .map(t -> TicketModel.builder()
                            .ticketName(t.getTicketName())
                            .price(BigDecimal.valueOf(t.getPrice()))
                            .quantity(t.getQuantity())
                            .event(event)
                            .build()
                    )
                    .toList();

            event.setTickets(tickets);
        }

        return eventRepository.save(event);
    }

    public List<EventListItemResponse> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> new EventListItemResponse(
                        event.getEventId(),
                        event.getTitle(),
                        event.getCategory(),
                        event.getDate(),
                        event.getLocation().getVenue(),
                        event.getLocation().getCity(),
                        event.getTickets().stream()
                                .mapToInt(t -> t.getPrice().intValue())
                                .min()
                                .orElse(0)
                ))
                .toList();
    }

    public List<EventListItemResponse> getEventsByCategory(EventCategories category) {
        List<EventModel> events = eventRepository.findByCategory(category);
        if (events.isEmpty()) {
            throw new CategoryEventNotFoundException();
        }
        return events.stream()
                .map(event -> new EventListItemResponse(
                        event.getEventId(),
                        event.getTitle(),
                        event.getCategory(),
                        event.getDate(),
                        event.getLocation().getVenue(),
                        event.getLocation().getCity(),
                        event.getTickets().stream()
                                .mapToInt(t -> t.getPrice().intValue())
                                .min()
                                .orElse(0)
                ))
                .toList();
    }


    public EventResponse getEventDetail(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException());

        return mapToEventResponse(event);
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException();
        }

        eventRepository.deleteById(eventId);
    }


    public String updateEventImage(Long eventId, MultipartFile image) {
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        String imageUrl = imageService.uploadEventImage(image);

        event.setImageUrl(imageUrl);
        eventRepository.save(event);

        return imageUrl;
    }

    public EventImageResponse getEventImage(Long eventId) {
        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (event.getImageUrl() == null || event.getImageUrl().isBlank()) {
            throw new EventNotFoundException();
        }

        return new EventImageResponse(
                event.getEventId(),
                event.getImageUrl()
        );
    }

    public EventResponse updateEvent(Long eventId, UpdateEventRequest request) {

        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        event.setTitle(request.getTitle());
        event.setShortSummary(request.getShortSummary());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setDate(request.getDate());
        event.setTime(request.getTime());
        event.setTotalCapacity(request.getTotalCapacity());

        LocationModel location = event.getLocation();
        location.setVenue(request.getVenue());
        location.setAddress(request.getAddress());
        location.setCity(request.getCity());
        location.setCountry(request.getCountry());

        eventRepository.save(event);

        return mapToEventResponse(event);
    }

    public EventResponse patchEvent(Long eventId, PatchEventRequest request) {

        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        if (request.getTitle() != null)
            event.setTitle(request.getTitle());

        if (request.getShortSummary() != null)
            event.setShortSummary(request.getShortSummary());

        if (request.getDescription() != null)
            event.setDescription(request.getDescription());

        if (request.getCategory() != null)
            event.setCategory(request.getCategory());

        if (request.getDate() != null)
            event.setDate(request.getDate());

        if (request.getTime() != null)
            event.setTime(request.getTime());

        if (request.getTotalCapacity() != null)
            event.setTotalCapacity(request.getTotalCapacity());

        LocationModel location = event.getLocation();
        if (request.getVenue() != null)
            location.setVenue(request.getVenue());
        if (request.getAddress() != null)
            location.setAddress(request.getAddress());
        if (request.getCity() != null)
            location.setCity(request.getCity());
        if (request.getCountry() != null)
            location.setCountry(request.getCountry());

        eventRepository.save(event);

        return mapToEventResponse(event);
    }

    private EventResponse mapToEventResponse(EventModel event) {
        return new EventResponse(
                event.getEventId(),
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
    }
}
