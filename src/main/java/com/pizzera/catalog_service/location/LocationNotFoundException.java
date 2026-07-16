package com.pizzera.catalog_service.location;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LocationNotFoundException extends ResponseStatusException {
    public LocationNotFoundException(Long locationId) {
        super(HttpStatus.NOT_FOUND, "Location with ID " + locationId + " not found");
    }
}
