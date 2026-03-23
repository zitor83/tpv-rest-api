package com.ortiz.tpv_backend.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
public final class Product {

    private final String id;
    private final String name;
    private final BigDecimal price;
    private final String category;
    private final String description;
    private final String imageUrl;
    private final boolean available;
    private final List<String> defaultIngredients;

    public Product(
        String id,
        String name,
        BigDecimal price,
        String category,
        String description,
        String imageUrl,
        Boolean available,
        List<String> defaultIngredients
    ) {
        this.id = validateId(id);
        this.name = validateText(name, "name");
        this.price = validatePrice(price);
        this.category = validateText(category, "category");
        this.description = validateText(description, "description");
        this.imageUrl = validateText(imageUrl, "imageUrl");
        this.available = available == null || available;
        this.defaultIngredients = sanitizeIngredients(defaultIngredients);
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Product other)) {
            return false;
        }
        if (id == null || other.id == null) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private static String validateId(String id) {
        if (id == null) {
            return null;
        }
        String normalized = id.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("id cannot be blank");
        }
        return normalized;
    }

    private static String validateText(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required");
        }

        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank");
        }
        return normalized;
    }

    private static BigDecimal validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("price is required");
        }
        if (price.signum() <= 0) {
            throw new IllegalArgumentException("price must be greater than 0");
        }
        return price;
    }

    private static List<String> sanitizeIngredients(List<String> ingredients) {
        if (ingredients == null) {
            return List.of();
        }

        return ingredients.stream()
            .map(ingredient -> validateText(ingredient, "defaultIngredient"))
            .toList();
    }
}

