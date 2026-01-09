package com.example.finalproject.event.service.event;

import com.example.finalproject.event.dto.request.event.CreateEventRequest;
import com.example.finalproject.event.dto.request.event.PatchEventRequest;
import com.example.finalproject.event.dto.request.event.UpdateEventRequest;
import com.example.finalproject.event.dto.response.event.*;
import com.example.finalproject.event.exception.event.CategoryEventNotFoundException;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.exception.event.NoActiveEventException;
import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventModel;
import com.example.finalproject.event.model.LocationModel;
import com.example.finalproject.event.model.TicketModel;
import com.example.finalproject.event.repository.EventRepository;
import com.example.finalproject.event.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public class EventService {

    private final ImageService imageService;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    public EventService(EventRepository eventRepository,
                        ImageService imageService,
                        LocationRepository locationRepository,
                        EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
        this.locationRepository = locationRepository;
        this.eventMapper = eventMapper;
    }
    public EventModel createEvent(CreateEventRequest request) {

        LocationModel location = LocationModel.builder()
                .venue(request.getVenue())
                .address(request.getAddress())
                .city(request.getCity())
                .country(request.getCountry())
                .build();

        locationRepository.save(location);

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
                            .build())
                    .toList();

            event.setTickets(tickets);
        }

        return eventRepository.save(event);
    }

    public List<EventListItemResponse> getAllEvents() {

        List<EventModel> events = eventRepository.findAllActiveEvents();

        if (events.isEmpty()) {
            throw new NoActiveEventException();
        }

        return events.stream()
                .map(eventMapper::toListItem)
                .toList();
    }

    public List<EventListItemResponse> getEventsByCategory(EventCategories category) {

        List<EventModel> events =
                eventRepository.findActiveEventsByCategory(category);

        if (events.isEmpty()) {
            throw new CategoryEventNotFoundException();
        }

        return events.stream()
                .map(eventMapper::toListItem)
                .toList();
    }


    public EventDetailResponse getEventDetail(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(EventNotFoundException::new);

        int remainingQuota = event.getTickets().stream()
                .mapToInt(TicketModel::getQuantity)
                .sum();

        EventDetailResponse response = eventMapper.toDetailResponse(event, remainingQuota);

        // warning jika sisa ≤ 10%
        int tenPercentThreshold =
                (int) Math.ceil(event.getTotalCapacity() * 0.1);

        if (remainingQuota > 0 && remainingQuota <= tenPercentThreshold) {
            response.setWarningMessage(
                    "Almost sold out! Only " + remainingQuota + " spots remaining."
            );
        }

        return response;
    }

    public void deleteEvent(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException();
        }

        eventRepository.deleteById(eventId);
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

        return eventMapper.toEventResponse(event);
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

        if (request.getTotalCapacity() != null) {
            event.setTotalCapacity(request.getTotalCapacity());
            if (request.getTotalCapacity() == 0 && event.getTickets() != null) {
                event.getTickets()
                        .forEach(ticket -> ticket.setQuantity(0));
            }
        }
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

        return eventMapper.toEventResponse(event);
    }
}
