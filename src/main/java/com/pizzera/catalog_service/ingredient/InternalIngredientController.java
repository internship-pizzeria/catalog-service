package com.pizzera.catalog_service.ingredient;

import com.pizzera.catalog_service.security.LocationContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/locations/{locationId}/ingredients")
@RequiredArgsConstructor
public class InternalIngredientController {

    private final IngredientService ingredientService;
    private final LocationContext locationContext;

    @GetMapping
    public List<LocationIngredientResponse> getAvailability(@PathVariable Long locationId) {
        validateAccess(locationId);
        return ingredientService.getAvailabilityForLocation(locationId);
    }

    @PatchMapping("/{ingredientId}")
    public LocationIngredient toggleAvailability(@PathVariable Long locationId,
                                                 @PathVariable Long ingredientId) {
        validateAccess(locationId);
        return ingredientService.toggleAvailability(locationId, ingredientId);
    }

    private void validateAccess(Long locationId) {
        Long currentLocationId = locationContext.getCurrentLocationId();
        if(currentLocationId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing location context");
        }
        if(!currentLocationId.equals(locationId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this location");
        }
    }
}