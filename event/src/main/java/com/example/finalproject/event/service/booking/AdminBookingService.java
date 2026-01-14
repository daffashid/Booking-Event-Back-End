package com.example.finalproject.event.service.booking;

import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingDetailResponse;
import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingListResponse;
import com.example.finalproject.event.exception.Booking.BookingNotFoundException;
import com.example.finalproject.event.exception.Booking.NoBookingsFoundException;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.model.booking.BookingModel;
import com.example.finalproject.event.repository.BookingRepository;
import com.example.finalproject.event.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminBookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final AdminBookingMapper mapper;

    public AdminBookingService(
            BookingRepository bookingRepository,
            EventRepository eventRepository,
            AdminBookingMapper mapper) {
        this.bookingRepository = bookingRepository;
        this.eventRepository = eventRepository;
        this.mapper = mapper;
    }

    public List<AdminBookingListResponse> getAllBookings() {

        List<BookingModel> bookings = bookingRepository.findAll();

        if (bookings.isEmpty()) {
            throw new NoBookingsFoundException();
        }

        return bookings.stream()
                .map(mapper::toListResponse)
                .toList();
    }

    public AdminBookingDetailResponse getBookingDetail(Long id) {

        BookingModel booking = bookingRepository.findById(id)
                .orElseThrow(BookingNotFoundException::new);

        return mapper.toDetailResponse(booking);
    }

    public List<AdminBookingListResponse> getBookingsByEvent(Long eventId) {

        eventRepository.findById(eventId)
                .orElseThrow(EventNotFoundException::new);

        List<BookingModel> bookings =
                bookingRepository.findByEvent_EventId(eventId);

        if (bookings.isEmpty()) {
            throw new NoBookingsFoundException();
        }

        return bookings.stream()
                .map(mapper::toListResponse)
                .toList();
    }
}
