package com.pizzera.catalog_service.ingredient;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "location_ingredients")
class LocationIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_id", nullable = false)
    private Long locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(nullable = false)
    private boolean available = true;

    public LocationIngredient(Long locationId, Ingredient ingredient, boolean available) {
        this.locationId = locationId;
        this.ingredient = ingredient;
        this.available = available;
    }

    public void toggleAvailability() {
        this.available = !this.available;
    }
}

