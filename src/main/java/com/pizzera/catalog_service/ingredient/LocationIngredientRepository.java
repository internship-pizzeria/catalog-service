package com.pizzera.catalog_service.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
interface LocationIngredientRepository extends JpaRepository<LocationIngredient, Long> {

    List<LocationIngredient> findByLocationId(Long locationId);

    Optional<LocationIngredient> findByLocationIdAndIngredientId(Long locationId, Long ingredientId);

    @Query("SELECT li.ingredient.id FROM LocationIngredient li WHERE li.locationId = :locationId AND li.available = false")
    List<Long> findUnavailableIngredientIds(@Param("locationId") Long locationId);

}