package com.pizzera.catalog_service.location;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String city;

    @Column
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

    public Location(String city, String postalCode, String street, String buildingNumber, String countryCode, String timezone) {
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.countryCode = countryCode;
        this.timezone = timezone;
        this.status = LocationStatus.ACTIVE;
    }
}
