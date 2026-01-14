package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.event.EventCategories;
import com.example.finalproject.event.model.event.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends JpaRepository<EventModel, Long> {
    List<EventModel> findByCategory(EventCategories category);

    /* =========================
       GET ALL ACTIVE EVENTS
       ========================= */
    @Query("""
        SELECT DISTINCT e
        FROM EventModel e
        JOIN e.tickets t
        GROUP BY e
        HAVING SUM(t.quantity) > 0
    """)
    List<EventModel> findAllActiveEvents();

    /* =========================
       GET ACTIVE EVENTS BY CATEGORY
       ========================= */
    @Query("""
    SELECT DISTINCT e
    FROM EventModel e
    LEFT JOIN e.tickets t
    WHERE e.category = :category
    GROUP BY e
    HAVING COALESCE(SUM(t.quantity), 0) > 0
""")
    List<EventModel> findActiveEventsByCategory(
            @Param("category") EventCategories category
    );

    /* =========================
       GET ACTIVE EVENTS BY SEARCH
       ========================= */
    @Query("""
SELECT DISTINCT e
FROM EventModel e
LEFT JOIN e.tickets t
WHERE
    LOWER(e.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(e.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
    OR LOWER(CAST(e.category AS string)) LIKE LOWER(CONCAT('%', :keyword, '%'))
GROUP BY e
HAVING COALESCE(SUM(t.quantity), 0) > 0
""")
    List<EventModel> searchEvents(@Param("keyword") String keyword);

}
