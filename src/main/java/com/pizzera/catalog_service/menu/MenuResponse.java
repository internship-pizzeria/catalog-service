package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.product.ProductResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;

@Schema(description = "Location-aware menu with product availability")
public record MenuResponse(
        @Schema(description = "All products with their availability status for this location")
        List<ProductResponse> products,
        @Schema(description = "Number of unavailable products", example = "2")
        int unavailableCount
) implements Serializable {}
