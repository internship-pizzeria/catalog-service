package com.pizzera.catalog_service.location;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/locations")
@RequiredArgsConstructor
class LocationController {

    private final LocationService locationService;

    @GetMapping
    public LocationPageResponse getAllActiveLocations(
            @RequestParam(required = false) String city,
            @PageableDefault(size = 20) Pageable pageable) {
        return locationService.getAllActiveLocations(city, pageable);
    }
}