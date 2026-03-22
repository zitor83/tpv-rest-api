package com.ortiz.tpv_backend.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class OrderLine {

    private final Long id;
    private final String productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final int quantity;
    private final BigDecimal subTotal;

    public OrderLine(Long id, String productId, String productName, BigDecimal unitPrice, int quantity) {
        this.id = validateId(id);
        this.productId = validateProductId(productId);
        this.productName = validateText(productName, "productName");
        this.unitPrice = validateUnitPrice(unitPrice);
        this.quantity = validateQuantity(quantity);
        this.subTotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }

    public Long getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    private static Long validateId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        return id;
    }

    private static String validateProductId(String productId) {
        if (productId == null) {
            throw new IllegalArgumentException("productId is required");
        }

        String normalized = productId.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("productId cannot be blank");
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

    private static BigDecimal validateUnitPrice(BigDecimal unitPrice) {
        Objects.requireNonNull(unitPrice, "unitPrice is required");
        if (unitPrice.signum() <= 0) {
            throw new IllegalArgumentException("unitPrice must be greater than 0");
        }
        return unitPrice;
    }

    private static int validateQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        return quantity;
    }
}

