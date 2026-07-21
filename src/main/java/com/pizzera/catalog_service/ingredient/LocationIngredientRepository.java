package com.pizzera.catalog_service.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationIngredientRepository extends JpaRepository<LocationIngredient, Long> {

    List<LocationIngredient> findByLocationId(Long locationId);

    Optional<LocationIngredient> findByLocationIdAndIngredientId(Long locationId, Long ingredientId);
}