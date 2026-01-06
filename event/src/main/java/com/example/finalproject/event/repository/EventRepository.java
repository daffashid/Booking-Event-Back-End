package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.EventCategories;
import com.example.finalproject.event.model.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<EventModel, Long> {
    List<EventModel> findByCategory(EventCategories category);
}
