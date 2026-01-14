package com.example.finalproject.event.model.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnlineEventModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long onlineEventId;

    @Column(nullable = false)
    private String platform;   // Zoom, Google Meet, YouTube, dll

    @Column(nullable = false)
    private String linkUrl;
}
