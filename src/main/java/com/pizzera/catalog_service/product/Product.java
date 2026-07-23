package com.pizzera.catalog_service.product;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "products")
class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductIngredient> ingredients;


    public Product(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
