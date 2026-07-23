package com.pizzera.catalog_service.location;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

@Schema(description = "Restaurant location details")
public record LocationResponse(
        @Schema(description = "Unique location identifier", example = "1")
        Long id,
        @Schema(description = "City name", example = "Kraków")
        String city,
        @Schema(description = "Postal code", example = "31-001")
        String postalCode,
        @Schema(description = "Street name", example = "Floriańska")
        String street,
        @Schema(description = "Building number", example = "10")
        String buildingNumber,
        @Schema(description = "ISO country code", example = "PL")
        String countryCode,
        @Schema(description = "IANA timezone", example = "Europe/Warsaw")
        String timezone,
        @Schema(description = "Current location status")
        LocationStatus status) implements Serializable {

    static LocationResponse from(Location location) {
        return new LocationResponse(
                location.getId(), location.getCity(), location.getPostalCode(),
                location.getStreet(), location.getBuildingNumber(), location.getCountryCode(),
                location.getTimezone(), location.getStatus()
        );
    }
}