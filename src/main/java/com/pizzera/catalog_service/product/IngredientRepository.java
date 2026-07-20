package com.pizzera.catalog_service.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}