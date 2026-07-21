package com.pizzera.catalog_service.ingredient;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class IngredientNotFoundException extends ResponseStatusException {

    public IngredientNotFoundException(Long id) {
        super(HttpStatus.NOT_FOUND, "Ingredient with ID " + id + " not found.");
    }
}