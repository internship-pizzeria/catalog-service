package com.pizzera.catalog_service.product;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductNotFoundException extends ResponseStatusException {
    public ProductNotFoundException(Long productId) {
        super(HttpStatus.NOT_FOUND, "Product with ID: " + productId + " not found.");
    }
}
