package com.pizzera.catalog_service.service;

import com.pizzera.catalog_service.dto.LocationResponse;
import com.pizzera.catalog_service.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationResponse> getAllActiveLocations() {
        return locationRepository.findByIsActiveTrue().stream()
                .map(location -> new LocationResponse(location.getId(), location.getCity(), location.getAddress()))
                .toList();
    }

}
