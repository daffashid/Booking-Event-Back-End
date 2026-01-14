package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.booking.BookingModel;
import com.example.finalproject.event.model.booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingModel, Long> {

    List<BookingModel> findByUser_UserId(Long userId);

    List<BookingModel> findByEvent_EventId(Long eventId);

    boolean existsByEvent_EventIdAndStatus(
            Long eventId,
            BookingStatus status
    );
}
