package com.pizzera.catalog_service.ingredient;

import com.pizzera.catalog_service.security.LocationContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/internal/locations/{locationId}/ingredients")
@RequiredArgsConstructor
@Tag(name = "Internal Ingredients", description = "Location-specific ingredient availability management (requires LocationId and X-User-Id headers)")
class InternalIngredientController {

    private final IngredientService ingredientService;
    private final LocationContext locationContext;

    @GetMapping
    @Operation(summary = "Get ingredient availability", description = "Returns all ingredients with their availability status for a specific location")
    public List<LocationIngredientResponse> getAvailability(
            @Parameter(description = "Location ID", example = "1") @PathVariable Long locationId,
            @Parameter(description = "Authenticated location ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("LocationId") String locationIdHeader,
            @Parameter(description = "Authenticated user ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("X-User-Id") String userIdHeader) {
        validateAccess(locationId);
        return ingredientService.getAvailabilityForLocation(locationId);
    }

    @PatchMapping("/{ingredientId}")
    @Operation(summary = "Toggle ingredient availability", description = "Toggles the availability of a specific ingredient at a location. If no record exists, creates one and sets it to unavailable.")
    public LocationIngredientResponse toggleAvailability(
            @Parameter(description = "Location ID", example = "1") @PathVariable Long locationId,
            @Parameter(description = "Ingredient ID", example = "5") @PathVariable Long ingredientId,
            @Parameter(description = "Authenticated location ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("LocationId") String locationIdHeader,
            @Parameter(description = "Authenticated user ID", example = "1", required = true, in = ParameterIn.HEADER) @RequestHeader("X-User-Id") String userIdHeader) {
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