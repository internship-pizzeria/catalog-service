package com.pizzera.catalog_service.location;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Location operating status")
public enum LocationStatus {
    @Schema(description = "Location is open and accepting orders")
    ACTIVE,
    @Schema(description = "Location is outside working hours")
    OUT_OF_WORKING_HOURS,
    @Schema(description = "Temporarily closed")
    TEMPORARILY_CLOSED,
    @Schema(description = "Under renovation")
    IN_RENOVATION,
    @Schema(description = "Permanently closed")
    PERMANENTLY_CLOSED
}