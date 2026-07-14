package com.pizzera.catalog_service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "locations")
    public Page<LocationResponse> getAllActiveLocations(String city, Pageable pageable) {
        if (city != null && !city.isBlank()) {
            String trimmedCity = city.trim();
            return locationRepository.findByStatusAndCityContainingIgnoreCase(LocationStatus.ACTIVE, trimmedCity, pageable)
                    .map(LocationResponse::new);
        }
        return locationRepository.findByStatus(LocationStatus.ACTIVE, pageable)
                .map(LocationResponse::new);
    }
}
