package com.pizzera.catalog_service.location;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface LocationRepository extends JpaRepository<Location, Long> {
    Page<Location> findByStatus(LocationStatus status, Pageable pageable);

    Page<Location> findByStatusAndCityContainingIgnoreCase(LocationStatus status, String city, Pageable pageable);
}
