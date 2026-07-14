package com.pizzera.catalog_service.location;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String buildingNumber;

    @Column(nullable = false)
    private String countryCode;

    @Column(nullable = false)
    private String timezone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationStatus status = LocationStatus.ACTIVE;

    @CreationTimestamp
    private Instant createdAt;
}
