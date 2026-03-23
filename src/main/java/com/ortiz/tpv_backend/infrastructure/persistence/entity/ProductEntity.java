package com.ortiz.tpv_backend.infrastructure.persistence.entity;

import com.ortiz.tpv_backend.infrastructure.persistence.converter.JsonListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;

    @Convert(converter = JsonListConverter.class)
    @Column(name = "default_ingredients", nullable = false)
    @Builder.Default
    private List<String> defaultIngredients = new ArrayList<>();
}

