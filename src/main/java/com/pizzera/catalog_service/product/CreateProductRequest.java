package com.pizzera.catalog_service.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(description = "Request body for creating a new product")
public record CreateProductRequest(
        @Schema(description = "Product name", example = "Margherita", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Product name is required")
        String name,

        @Schema(description = "Product description", example = "Classic pizza with mozzarella and basil")
        String description,

        @Schema(description = "Product price in PLN", example = "25.00", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be positive")
        BigDecimal price
) {}