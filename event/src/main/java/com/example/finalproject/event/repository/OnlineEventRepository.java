package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.OnlineEventModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnlineEventRepository extends JpaRepository<OnlineEventModel, Long> {
}
