package com.pizzera.catalog_service.location;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long locationId) {
        super("Location with ID " + locationId + " not found");
    }
}
