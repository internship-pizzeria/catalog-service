package com.pizzera.catalog_service.menu;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
@Tag(name = "Menu", description = "Location-aware menu with ingredient availability")
class MenuController {

    private final MenuService menuService;

    @GetMapping
    @Operation(summary = "Get menu for location", description = "Returns all products with their availability status for a specific location. Products with unavailable ingredients are marked as unavailable.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu for the location"),
            @ApiResponse(responseCode = "404", description = "Location with the given ID does not exist")
    })
    public MenuResponse getMenu(
            @Parameter(description = "Location ID", example = "1", required = true) @RequestParam Long locationId) {
        return menuService.getMenuForLocation(locationId);
    }
}
