package com.pizzera.catalog_service.service;

import com.pizzera.catalog_service.dto.LocationResponse;
import com.pizzera.catalog_service.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;


    public Page<LocationResponse> getAllActiveLocations(Pageable pageable) {
        return locationRepository.findByIsActiveTrue(pageable)
                .map(LocationResponse::new);
    }

}
