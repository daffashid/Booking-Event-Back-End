package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.event.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketModel, Long> {

    List<TicketModel> findByEvent_EventId(Long eventId);
}
