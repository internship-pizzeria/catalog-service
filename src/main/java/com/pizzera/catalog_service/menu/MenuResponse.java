package com.pizzera.catalog_service.menu;

import com.pizzera.catalog_service.product.ProductResponse;

import java.io.Serializable;
import java.util.List;

public record MenuResponse(
        List<ProductResponse> products,
        int totalFiltered
) implements Serializable {
}
