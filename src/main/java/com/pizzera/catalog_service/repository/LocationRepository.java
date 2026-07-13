package com.pizzera.catalog_service.repository;

import com.pizzera.catalog_service.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByIsActiveTrue();

}
