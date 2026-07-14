package com.pizzera.catalog_service.dto;


import com.pizzera.catalog_service.entity.Location;

public record LocationResponse(Long id, String city, String street, String buildingNumber, String countryCode) {
    public LocationResponse(Location location) {
        this(
                location.getId(),
                location.getCity(),
                location.getStreet(),
                location.getBuildingNumber(),
                location.getCountryCode()
        );
    }
}
