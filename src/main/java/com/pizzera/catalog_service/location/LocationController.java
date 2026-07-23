package com.pizzera.catalog_service.location;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Locations", description = "Restaurant locations management")
class LocationController {

    private final LocationService locationService;

    @GetMapping
    @Operation(summary = "List active locations", description = "Returns a paginated list of all active restaurant locations, optionally filtered by city name")
    public LocationPageResponse getAllActiveLocations(
            @Parameter(description = "City name to filter locations by") @RequestParam(required = false) String city,
            @PageableDefault(size = 20) Pageable pageable) {
        return locationService.getAllActiveLocations(city, pageable);
    }
}