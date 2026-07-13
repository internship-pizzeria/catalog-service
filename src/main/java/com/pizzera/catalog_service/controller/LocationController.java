package com.pizzera.catalog_service.controller;


import com.pizzera.catalog_service.dto.LocationResponse;
import com.pizzera.catalog_service.service.LocationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationResponse> getAllActiveLocations() {
        return locationService.getAllActiveLocations();
    }
}