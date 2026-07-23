package com.pizzera.catalog_service.location;

import java.io.Serializable;

public record LocationResponse(Long id, String city, String postalCode, String street, String buildingNumber,
                               String countryCode, String timezone, LocationStatus status) implements Serializable {
}