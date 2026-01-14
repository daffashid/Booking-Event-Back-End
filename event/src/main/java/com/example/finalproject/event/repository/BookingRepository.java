package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.booking.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingModel, Long> {

    List<BookingModel> findByUser_UserId(Long userId);
}
