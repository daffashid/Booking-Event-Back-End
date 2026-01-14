package com.example.finalproject.event.repository;

import com.example.finalproject.event.model.event.LocationModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationModel, Long> {

    // OPTIONAL (untuk improvement nanti)
    Optional<LocationModel> findByVenueAndAddressAndCity(
            String venue,
            String address,
            String city
    );
}
