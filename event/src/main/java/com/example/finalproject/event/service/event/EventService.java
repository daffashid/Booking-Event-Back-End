package com.example.finalproject.event.service.event;

import com.example.finalproject.event.dto.request.event.CreateEventRequest;
import com.example.finalproject.event.dto.request.event.PatchEventRequest;
import com.example.finalproject.event.dto.request.event.UpdateEventRequest;
import com.example.finalproject.event.dto.response.event.*;
import com.example.finalproject.event.exception.Booking.EventHasPaidBookingException;
import com.example.finalproject.event.exception.event.*;
import com.example.finalproject.event.model.booking.BookingStatus;
import com.example.finalproject.event.model.event.*;
import com.example.finalproject.event.repository.BookingRepository;
import com.example.finalproject.event.repository.EventRepository;
import com.example.finalproject.event.repository.LocationRepository;
import com.example.finalproject.event.repository.OnlineEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;
    private final OnlineEventRepository onlineEventRepository;
    private final BookingRepository bookingRepository;

    public EventService(
            EventRepository eventRepository,
            LocationRepository locationRepository,
            EventMapper eventMapper,
            OnlineEventRepository onlineEventRepository,
            BookingRepository bookingRepository
    ) {
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.eventMapper = eventMapper;
        this.onlineEventRepository = onlineEventRepository;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public EventResponse createEvent(CreateEventRequest request) {

        EventModel.EventModelBuilder eventBuilder = EventModel.builder()
                .title(request.getTitle())
                .shortSummary(request.getShortSummary())
                .description(request.getDescription())
                .category(request.getCategory())
                .eventType(request.getEventType())
                .imageUrl(request.getImageUrl())
                .date(request.getDate())
                .time(request.getTime())
                .totalCapacity(request.getTotalCapacity());

        // ===== OFFLINE =====
        if (request.getEventType() == EventType.OFFLINE) {

            LocationModel location = LocationModel.builder()
                    .venue(request.getVenue())
                    .address(request.getAddress())
                    .city(request.getCity())
                    .country(request.getCountry())
                    .build();

            locationRepository.save(location);
            eventBuilder.location(location);
        }

        // ===== ONLINE =====
        if (request.getEventType() == EventType.ONLINE) {

            OnlineEventModel onlineEvent = OnlineEventModel.builder()
                    .platform(request.getPlatform())
                    .linkUrl(request.getLinkUrl())
                    .build();

            onlineEventRepository.save(onlineEvent);
            eventBuilder.onlineEvent(onlineEvent);
        }

        EventModel event = eventBuilder.build();

        // ===== TICKETS =====
        int totalTicketQty = request.getTickets().stream()
                .mapToInt(CreateEventRequest.TicketRequest::getQuantity)
                .sum();

        if (totalTicketQty != request.getTotalCapacity()) {
            throw new IllegalArgumentException("Ticket quantity must match capacity");
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
        EventModel savedEvent = eventRepository.save(event);
        return eventMapper.toEventResponse(savedEvent);
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

    @Transactional
    public void deleteEvent(Long eventId) {

        EventModel event = eventRepository
                .findByEventIdAndDeletedAtIsNull(eventId)
                .orElseThrow(EventNotFoundException::new);

        boolean hasPaidBooking =
                bookingRepository.existsByEvent_EventIdAndStatus(
                        eventId,
                        BookingStatus.PAID
                );

        if (hasPaidBooking) {
            throw new EventHasPaidBookingException();
        }

        event.setDeletedAt(LocalDateTime.now());
        eventRepository.save(event);
    }


    // =========================
    // PUT (FULL UPDATE)
    // =========================
    @Transactional
    public EventResponse updateEvent(Long eventId, UpdateEventRequest request) {

        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        // ===== BASIC =====
        event.setTitle(request.getTitle());
        event.setShortSummary(request.getShortSummary());
        event.setDescription(request.getDescription());
        event.setCategory(request.getCategory());
        event.setImageUrl(request.getImageUrl());
        event.setDate(request.getDate());
        event.setTime(request.getTime());
        event.setTotalCapacity(request.getTotalCapacity());

        // ===== OFFLINE =====
        if (event.getEventType() == EventType.OFFLINE) {
            if (request.getPlatform() != null || request.getLinkUrl() != null) {
                throw new InvalidEventTypeFieldException();
            }
            LocationModel location = event.getLocation();

            location.setVenue(request.getVenue());
            location.setAddress(request.getAddress());
            location.setCity(request.getCity());
            location.setCountry(request.getCountry());
        }

        // ===== ONLINE =====
        if (event.getEventType() == EventType.ONLINE) {
            if (request.getVenue() != null || request.getAddress() != null ||
                    request.getCity() != null || request.getCountry() != null) {
                throw new InvalidEventTypeFieldException();
            }
            OnlineEventModel online = event.getOnlineEvent();

            online.setPlatform(request.getPlatform());
            online.setLinkUrl(request.getLinkUrl());
        }

        // ===== TICKETS =====
        int totalQty = request.getTickets().stream()
                .mapToInt(UpdateEventRequest.TicketRequest::getQuantity)
                .sum();

        if (totalQty != request.getTotalCapacity()) {
            throw new IllegalArgumentException("Ticket quantity must match capacity");
        }

        event.getTickets().clear();

        List<TicketModel> tickets = request.getTickets().stream()
                .map(t -> TicketModel.builder()
                        .ticketName(t.getTicketName())
                        .price(BigDecimal.valueOf(t.getPrice()))
                        .quantity(t.getQuantity())
                        .event(event)
                        .build()
                ).toList();

        event.getTickets().addAll(tickets);

        return eventMapper.toEventResponse(eventRepository.save(event));
    }

    // =========================
    // PATCH (PARTIAL UPDATE)
    // =========================
    @Transactional
    public EventResponse patchEvent(Long eventId, PatchEventRequest request) {

        EventModel event = eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        // ===== BASIC =====
        if (request.getTitle() != null)
            event.setTitle(request.getTitle());

        if (request.getShortSummary() != null)
            event.setShortSummary(request.getShortSummary());

        if (request.getDescription() != null)
            event.setDescription(request.getDescription());

        if (request.getCategory() != null)
            event.setCategory(request.getCategory());

        if (request.getImageUrl() != null)
            event.setImageUrl(request.getImageUrl());

        if (request.getDate() != null)
            event.setDate(request.getDate());

        if (request.getTime() != null)
            event.setTime(request.getTime());

        if (request.getTotalCapacity() != null)
            event.setTotalCapacity(request.getTotalCapacity());

        // ===== OFFLINE PATCH =====
        if (event.getEventType() == EventType.OFFLINE && event.getLocation() != null) {
            if (request.getPlatform() != null || request.getLinkUrl() != null) {
                throw new InvalidEventTypeFieldException();
            }
            if (request.getVenue() != null)
                event.getLocation().setVenue(request.getVenue());

            if (request.getAddress() != null)
                event.getLocation().setAddress(request.getAddress());

            if (request.getCity() != null)
                event.getLocation().setCity(request.getCity());

            if (request.getCountry() != null)
                event.getLocation().setCountry(request.getCountry());
        }

        // ===== ONLINE PATCH =====
        if (event.getEventType() == EventType.ONLINE && event.getOnlineEvent() != null) {
            if (request.getVenue() != null || request.getAddress() != null ||
                    request.getCity() != null || request.getCountry() != null) {
                throw new InvalidEventTypeFieldException();
            }
            if (request.getPlatform() != null)
                event.getOnlineEvent().setPlatform(request.getPlatform());

            if (request.getLinkUrl() != null)
                event.getOnlineEvent().setLinkUrl(request.getLinkUrl());
        }

        // ===== TICKETS PATCH =====
        if (request.getTickets() != null) {

            int capacity = request.getTotalCapacity() != null
                    ? request.getTotalCapacity()
                    : event.getTotalCapacity();

            int totalQty = request.getTickets().stream()
                    .mapToInt(PatchEventRequest.PatchTicketRequest::getQuantity)
                    .sum();

            if (totalQty > capacity) {
                throw new IllegalArgumentException("Ticket quantity exceeds capacity");
            }

            event.getTickets().clear();

            event.getTickets().addAll(
                    request.getTickets().stream()
                            .map(t -> TicketModel.builder()
                                    .ticketName(t.getTicketName())
                                    .price(BigDecimal.valueOf(t.getPrice()))
                                    .quantity(t.getQuantity())
                                    .event(event)
                                    .build()
                            ).toList()
            );
        }

        return eventMapper.toEventResponse(eventRepository.save(event));
    }



    public List<EventListItemResponse> searchEvents (String keyword){
        if (keyword == null || keyword.trim().isEmpty()){
            throw new IllegalArgumentException("Keyword must not be empty");
        }

        List<EventModel> events = eventRepository.searchEvents(keyword);

        if (events.isEmpty()){
            throw new EventSearchNotFoundException();
        }
        return events.stream()
                .map(eventMapper::toListItem)
                .toList();
    }
}
