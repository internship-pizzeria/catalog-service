package com.pizzera.catalog_service.controller;


import com.pizzera.catalog_service.dto.LocationResponse;
import com.pizzera.catalog_service.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public Page<LocationResponse> getAllActiveLocations(@PageableDefault(size = 20) Pageable pageable) {
        return locationService.getAllActiveLocations(pageable);
    }
}