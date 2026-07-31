package com.pizzera.catalog_service.location;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return locationRepository.existsById(id);
    }

    public void ensureExists(Long id) {
        if (!locationRepository.existsById(id)) {
            throw new LocationNotFoundException(id);
        }
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "locations")
    public LocationPageResponse getAllActiveLocations(String city, Pageable pageable) {
        Page<LocationResponse> page;
        if (city != null && !city.isBlank()) {
            String trimmedCity = city.trim();
            page = locationRepository.findByStatusAndCityContainingIgnoreCase(LocationStatus.ACTIVE, trimmedCity, pageable)
                    .map(LocationResponse::from);
        } else {
            page = locationRepository.findByStatus(LocationStatus.ACTIVE, pageable)
                    .map(LocationResponse::from);
        }

        return LocationPageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public List<LocationResponse> getLocationsByIds(List<Long> ids) {
        return locationRepository.findAllById(ids).stream()
                .map(LocationResponse::from)
                .toList();
    }
}
