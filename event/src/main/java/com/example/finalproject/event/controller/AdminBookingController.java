package com.example.finalproject.event.controller;

import com.example.finalproject.event.dto.response.BaseResponse;
import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingDetailResponse;
import com.example.finalproject.event.dto.response.Booking.admin.AdminBookingListResponse;
import com.example.finalproject.event.exception.Booking.BookingNotFoundException;
import com.example.finalproject.event.exception.Booking.NoBookingsFoundException;
import com.example.finalproject.event.exception.event.EventNotFoundException;
import com.example.finalproject.event.service.booking.AdminBookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/bookings")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBookingController {

    private final AdminBookingService service;

    public AdminBookingController(AdminBookingService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<AdminBookingListResponse>>> getAllBookings(
            @RequestParam(required = false) Long eventId
    ) {

        BaseResponse<List<AdminBookingListResponse>> response = new BaseResponse<>();

        try {
            if (eventId != null) {
                response.setData(service.getBookingsByEvent(eventId));
            } else {
                response.setData(service.getAllBookings());
            }

            response.setSuccess(true);
            response.setMessage("Bookings fetched successfully");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (EventNotFoundException | NoBookingsFoundException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage("Failed to load bookings");
            response.setErrorCode("99");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<AdminBookingDetailResponse>> getBookingDetail(
            @PathVariable Long id
    ) {
        BaseResponse<AdminBookingDetailResponse> response = new BaseResponse<>();

        try {
            response.setData(service.getBookingDetail(id));
            response.setSuccess(true);
            response.setMessage("Booking detail fetched");
            response.setErrorCode("00");

            return ResponseEntity.ok(response);

        } catch (BookingNotFoundException e) {

            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setErrorCode("01");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {

            response.setSuccess(false);
            response.setMessage("Failed to load booking detail");
            response.setErrorCode("99");

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }
    }
}
