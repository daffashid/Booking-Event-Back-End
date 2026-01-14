package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.request.booking.CreateBookingRequest;
import com.example.finalproject.event.dto.response.BaseResponse;
import com.example.finalproject.event.dto.response.Booking.user.BookingResponse;
import com.example.finalproject.event.dto.response.Booking.user.BookingDetailResponse;
import com.example.finalproject.event.exception.Booking.*;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.service.booking.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BaseResponse<BookingResponse>> createBooking(
            @Valid @RequestBody CreateBookingRequest request
    ) {
        BaseResponse<BookingResponse> response = new BaseResponse<>();

        try {
            BookingResponse result = bookingService.createBooking(request);

            response.setSuccess(true);
            response.setMessage("Booking created successfully");
            response.setErrorCode("00");
            response.setData(result);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (TotalTicketLimitExceededException |
                 InvalidTicketQuantityException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (TicketNotFoundException |
                 EventNotFoundException |
                 BookingNotFoundException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (TicketNotBelongToEventException |
                 InvalidBookingStatusException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("03");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (InsufficientTicketStockException |
                 EventSoldOutException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to create booking");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BaseResponse<List<BookingResponse>>> getMyBookings() {

        BaseResponse<List<BookingResponse>> response = new BaseResponse<>();

        try {
            List<BookingResponse> bookings = bookingService.getMyBookings();

            if (bookings.isEmpty()) {
                response.setSuccess(true);
                response.setMessage("You have no orders yet");
                response.setErrorCode("00");
                response.setData(bookings);

                return ResponseEntity.ok(response);
            }

            response.setSuccess(true);
            response.setMessage("Bookings fetched successfully");
            response.setErrorCode("00");
            response.setData(bookings);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to fetch bookings");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BaseResponse<BookingDetailResponse>> getBookingDetail(
            @PathVariable Long id
    ) {
        BaseResponse<BookingDetailResponse> response = new BaseResponse<>();

        try {
            response.setData(bookingService.getBookingDetail(id));
            response.setSuccess(true);
            response.setMessage("Booking fetched successfully");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (BookingNotFoundException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (UnauthorizedBookingAccessException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("03");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to fetch booking detail");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }


    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BaseResponse<BookingResponse>> cancelBooking(
            @PathVariable Long id
    ) {
        BaseResponse<BookingResponse> response = new BaseResponse<>();

        try {
            response.setData(bookingService.cancelBooking(id));
            response.setSuccess(true);
            response.setMessage("Booking cancelled successfully");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (BookingNotFoundException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (InvalidBookingStatusException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("03");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Failed to cancel booking");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @PostMapping("/{id}/pay")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BaseResponse<BookingDetailResponse>> payBooking(
            @PathVariable Long id
    ) {
        BaseResponse<BookingDetailResponse> response = new BaseResponse<>();

        try {
            response.setData(bookingService.payBooking(id));
            response.setSuccess(true);
            response.setMessage("Payment successful");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (BookingNotFoundException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (UnauthorizedBookingAccessException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("03");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        } catch (BookingAlreadyPaidException |
                 InvalidBookingStatusException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("02");

            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage("Payment failed");
            response.setErrorCode("99");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}

