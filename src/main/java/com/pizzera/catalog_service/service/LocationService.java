package com.pizzera.catalog_service.service;

import com.pizzera.catalog_service.dto.LocationResponse;
import com.pizzera.catalog_service.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public Page<LocationResponse> getAllActiveLocations(String city, Pageable pageable) {
        if (city != null && !city.isBlank()) {
            return locationRepository.findByIsActiveTrueAndCityContainingIgnoreCase(city, pageable)
                    .map(LocationResponse::new);
        }
        return locationRepository.findByIsActiveTrue(pageable)
                .map(LocationResponse::new);
    }
}
