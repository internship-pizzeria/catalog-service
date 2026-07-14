package com.pizzera.catalog_service.repository;

import com.pizzera.catalog_service.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Page<Location> findByIsActiveTrue(Pageable pageable);

    Page<Location> findByIsActiveTrueAndCityContainingIgnoreCase(String city, Pageable pageable);

}
