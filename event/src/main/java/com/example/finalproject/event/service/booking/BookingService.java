package com.example.finalproject.event.service.booking;

import com.example.finalproject.event.config.SecurityUtil;
import com.example.finalproject.event.dto.request.booking.CreateBookingRequest;
import com.example.finalproject.event.dto.response.Booking.user.BookingResponse;
import com.example.finalproject.event.dto.response.Booking.user.BookingDetailResponse;
import com.example.finalproject.event.exception.Booking.*;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.exception.user.UserNotFoundException;
import com.example.finalproject.event.model.booking.BookingItemModel;
import com.example.finalproject.event.model.booking.BookingModel;
import com.example.finalproject.event.model.booking.BookingStatus;
import com.example.finalproject.event.model.event.EventModel;
import com.example.finalproject.event.model.event.TicketModel;
import com.example.finalproject.event.model.user.UserModel;
import com.example.finalproject.event.repository.BookingRepository;
import com.example.finalproject.event.repository.EventRepository;
import com.example.finalproject.event.repository.TicketRepository;
import com.example.finalproject.event.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final BookingMapper bookingMapper;
    private final QrCodeService qrCodeService;

    public BookingService(
            BookingRepository bookingRepository,
            UserRepository userRepository,
            EventRepository eventRepository,
            TicketRepository ticketRepository,
            BookingMapper bookingMapper,
            QrCodeService qrCodeService) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.bookingMapper = bookingMapper;
        this.qrCodeService = qrCodeService;
    }

    @Transactional
    public BookingResponse createBooking(CreateBookingRequest request) {

        String email = SecurityUtil.getCurrentUserEmail();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        EventModel event = eventRepository.findById(request.getEventId())
                .orElseThrow(EventNotFoundException::new);

        int totalQty = request.getTickets()
                .stream()
                .mapToInt(CreateBookingRequest.TicketOrderRequest::getQuantity)
                .sum();

        if (totalQty < 1 || totalQty > 10) {
            throw new TotalTicketLimitExceededException();
        }

        BookingModel booking = BookingModel.builder()
                .user(user)
                .event(event)
                .status(BookingStatus.PENDING)
                .build();
        List<BookingItemModel> items = request.getTickets().stream().map(req -> {

            TicketModel ticket = ticketRepository.findById(req.getTicketId())
                    .orElseThrow(TicketNotFoundException::new);

            if (!ticket.getEvent().getEventId().equals(event.getEventId())) {
                throw new TicketNotBelongToEventException();
            }

            if (ticket.getQuantity() == 0) {
                throw new EventSoldOutException();
            }

            if (ticket.getQuantity() < req.getQuantity()) {
                throw new InsufficientTicketStockException();
            }

            ticket.setQuantity(ticket.getQuantity() - req.getQuantity());

            return BookingItemModel.builder()
                    .booking(booking)
                    .ticket(ticket)
                    .quantity(req.getQuantity())
                    .priceSnapshot(ticket.getPrice())
                    .build();

        }).toList();

        BigDecimal totalPrice = items.stream()
                .map(item ->
                        item.getPriceSnapshot()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        booking.setItems(items);
        booking.setTotalPrice(totalPrice);

        bookingRepository.save(booking);

        return bookingMapper.toResponse(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingResponse> getMyBookings() {

        String email = SecurityUtil.getCurrentUserEmail();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        return bookingRepository.findByUser_UserId(user.getUserId())
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Transactional
    public BookingDetailResponse getBookingDetail(Long bookingId) {

        String email = SecurityUtil.getCurrentUserEmail();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        BookingModel booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        if (!booking.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedBookingAccessException();
        }

        return bookingMapper.toDetailResponse(booking);
    }


    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {

        BookingModel booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStatusException();
        }

        for (BookingItemModel item : booking.getItems()) {
            TicketModel ticket = item.getTicket();
            ticket.setQuantity(ticket.getQuantity() + item.getQuantity());
        }

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);

        return bookingMapper.toResponse(booking);
    }

    @Transactional
    public BookingDetailResponse payBooking(Long bookingId) {

        String email = SecurityUtil.getCurrentUserEmail();
        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);

        BookingModel booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingNotFoundException::new);

        if (!booking.getUser().getUserId().equals(user.getUserId())) {
            throw new UnauthorizedBookingAccessException();
        }

        if (booking.getStatus() == BookingStatus.PAID) {
            throw new BookingAlreadyPaidException();
        }

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new InvalidBookingStatusException();
        }

        String qrContent = "BOOKING-" + booking.getBookingId() + "-" + user.getEmail();
        String qrBase64 = qrCodeService.generateQrBase64(qrContent);

        booking.setPaidAt(LocalDateTime.now());
        booking.setQrCode(qrBase64);
        booking.setStatus(BookingStatus.PAID);

        bookingRepository.save(booking);

        return bookingMapper.toDetailResponse(booking);
    }


}
