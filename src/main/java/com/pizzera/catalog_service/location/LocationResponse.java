package com.pizzera.catalog_service.location;

public record LocationResponse(Long id, String city, String postalCode, String street, String buildingNumber, String countryCode, String timezone, LocationStatus status) {
    public LocationResponse(Location location) {
        this(
                location.getId(),
                location.getCity(),
                location.getPostalCode(),
                location.getStreet(),
                location.getBuildingNumber(),
                location.getCountryCode(),
                location.getTimezone(),
                location.getStatus()
        );
    }
}