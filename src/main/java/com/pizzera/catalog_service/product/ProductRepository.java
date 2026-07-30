package com.pizzera.catalog_service.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.ingredients pi LEFT JOIN FETCH pi.ingredient")
    List<Product> findAllWithIngredients();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ingredients pi LEFT JOIN FETCH pi.ingredient WHERE p.id = :id")
    Optional<Product> findByIdWithIngredients(@Param("id") Long id);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.ingredients pi LEFT JOIN FETCH pi.ingredient WHERE p.id IN :ids")
    List<Product> findAllByIdWithIngredients(@Param("ids") List<Long> ids);

}
