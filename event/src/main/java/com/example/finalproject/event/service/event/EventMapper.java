package com.example.finalproject.event.service.event;

import com.example.finalproject.event.dto.response.event.*;
import com.example.finalproject.event.model.EventModel;
import com.example.finalproject.event.model.TicketModel;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    /* =========================
       CREATE / UPDATE RESPONSE
       ========================= */
    public EventResponse toEventResponse(EventModel event) {
        return new EventResponse(
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

    /* =========================
       LIST ITEM RESPONSE
       ========================= */
    public EventListItemResponse toListItem(EventModel event) {
        int minPrice = event.getTickets() == null || event.getTickets().isEmpty()
                ? 0
                : event.getTickets().stream()
                .mapToInt(t -> t.getPrice().intValue())
                .min()
                .orElse(0);
        return new EventListItemResponse(
                event.getTitle(),
                event.getCategory(),
                event.getDate(),
                event.getLocation().getVenue(),
                event.getLocation().getCity(),
                minPrice
        );
    }

    /* =========================
       DETAIL RESPONSE
       ========================= */
    public EventDetailResponse toDetailResponse(
            EventModel event,
            int remainingQuota
    ) {
        return EventDetailResponse.builder()
                .title(event.getTitle())
                .shortSummary(event.getShortSummary())
                .description(event.getDescription())
                .imageUrl(event.getImageUrl())
                .date(event.getDate())
                .time(event.getTime())
                .category(event.getCategory())
                .totalCapacity(event.getTotalCapacity())
                .remainingCapacity(remainingQuota)
                .location(new LocationResponse(
                        event.getLocation().getVenue(),
                        event.getLocation().getCity()
                ))
                .tickets(
                        event.getTickets().stream()
                                .map(t -> new TicketResponse(
                                        t.getTicketName(),
                                        t.getPrice(),
                                        t.getQuantity()
                                ))
                                .toList()
                )
                .warningMessage(null)
                .build();
    }

    public EventListAdminResponse toAdminList(EventModel event) {

        int remainingQuota = event.getTickets() == null
                ? event.getTotalCapacity()
                : event.getTickets().stream()
                .mapToInt(TicketModel::getQuantity)
                .sum();

        int minPrice = event.getTickets() == null || event.getTickets().isEmpty()
                ? 0
                : event.getTickets().stream()
                .mapToInt(t -> t.getPrice().intValue())
                .min()
                .orElse(0);

        return new EventListAdminResponse(
                event.getEventId(),
                event.getTitle(),
                event.getCategory(),
                event.getDate(),
                event.getLocation().getVenue(),
                event.getLocation().getCity(),
                minPrice,
                event.getTotalCapacity(),
                remainingQuota
        );
    }
}
