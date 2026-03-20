package com.ortiz.tpv_backend.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Product {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final String category;

    public Product(Long id, String name, BigDecimal price, String category) {
        this.id = validateId(id);
        this.name = validateText(name, "name");
        this.price = validatePrice(price);
        this.category = validateText(category, "category");
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Product other)) {
            return false;
        }
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private static Long validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id is required");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        return id;
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
}

