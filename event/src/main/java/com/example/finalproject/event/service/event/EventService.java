package com.example.finalproject.event.service.event;

import com.example.finalproject.event.dto.event.CreateEventRequest;
import com.example.finalproject.event.dto.event.PatchEventRequest;
import com.example.finalproject.event.dto.event.TicketRequest;
import com.example.finalproject.event.dto.event.UpdateEventRequest;
import com.example.finalproject.event.exception.event.*;
import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventModel;
import com.example.finalproject.event.model.LocationModel;
import com.example.finalproject.event.model.TicketModel;
import com.example.finalproject.event.repository.EventRepository;
import com.example.finalproject.event.response.event.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class EventService {

    private final ImageService imageService;
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventService(EventRepository eventRepository,
                        ImageService imageService) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
    }

    public EventModel createEvent(CreateEventRequest request, MultipartFile image) {

        // ===== DATE =====
        LocalDate date;
        try {
            date = LocalDate.parse(request.getDate());
        } catch (Exception e) {
            throw new InvalidDateException();
        }

        if (!date.isAfter(LocalDate.now())) {
            throw new InvalidDateException();
        }

        // ===== TIME =====
        LocalTime time;
        try {
            time = LocalTime.parse(request.getTime());
        } catch (Exception e) {
            throw new InvalidTimeException();
        }

        // ===== TICKETS =====
        List<TicketRequest> ticketRequests = request.getTickets();
        if (ticketRequests == null || ticketRequests.isEmpty()) {
            throw new InvalidTicketException("At least one ticket is required");
        }

        // ===== IMAGE =====
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                imageUrl = imageService.uploadEventImage(image);
            } catch (Exception e) {
                throw new ImageUploadException();
            }
        }

        // ===== LOCATION =====
        LocationModel location = new LocationModel();
        location.setVenue(request.getVenue());
        location.setAddress(request.getAddress());
        location.setCity(request.getCity());
        location.setCountry(request.getCountry());

        // ===== EVENT =====
        EventModel event = new EventModel();
        event.setTitle(request.getTitle());
        event.setShortSummary(request.getShortSummary());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setDate(date);
        event.setTime(time);
        event.setTotalCapacity(request.getTotalCapacity());
        event.setImageUrl(imageUrl);
        event.setLocation(location);

        // ===== TICKET MODEL =====
        List<TicketModel> tickets = ticketRequests.stream().map(tr -> {
            TicketModel ticket = new TicketModel();
            ticket.setTicketName(tr.getTicketName());
            ticket.setPrice(BigDecimal.valueOf(tr.getPrice()));
            ticket.setQuantity(tr.getQuantity());
            ticket.setEvent(event);
            return ticket;
        }).toList();

        event.setTickets(tickets);

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

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = imageService.uploadEventImage(request.getImage());
            event.setImageUrl(imageUrl);
        }

        return mapToEventResponse(eventRepository.save(event));
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

        if (request.getImage() != null && !request.getImage().isEmpty()) {
            String imageUrl = imageService.uploadEventImage(request.getImage());
            event.setImageUrl(imageUrl);
        }

        return mapToEventResponse(eventRepository.save(event));
    }

    private EventResponse mapToEventResponse(EventModel event) {
        return new EventResponse(
                event.getEventId(),
                event.getTitle(),
                event.getCategory(),
                event.getDate(),
                event.getTime(),
                event.getImageUrl(),
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
