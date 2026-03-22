package com.ortiz.tpv_backend.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Objects;

public final class OrderLine {

    private final Long id;
    private final String productId;
    private final String productName;
    private final BigDecimal unitPrice;
    private final int quantity;
    private final BigDecimal subTotal;
    private final String notes;
    private final Map<String, List<String>> selectedIngredients;

    @Default
    public OrderLine(
        Long id,
        String productId,
        String productName,
        BigDecimal unitPrice,
        int quantity,
        String notes,
        Map<String, List<String>> selectedIngredients
    ) {
        this.id = validateId(id);
        this.productId = validateProductId(productId);
        this.productName = validateText(productName, "productName");
        this.unitPrice = validateUnitPrice(unitPrice);
        this.quantity = validateQuantity(quantity);
        this.subTotal = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
        this.notes = validateOptionalText(notes, "notes");
        this.selectedIngredients = sanitizeSelectedIngredients(selectedIngredients);
    }

    public OrderLine(Long id, String productId, String productName, BigDecimal unitPrice, int quantity) {
        this(id, productId, productName, unitPrice, quantity, null, null);
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

    public String getNotes() {
        return notes;
    }

    public Map<String, List<String>> getSelectedIngredients() {
        return selectedIngredients;
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

    private static String validateOptionalText(String value, String fieldName) {
        if (value == null) {
            return null;
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

    private static Map<String, List<String>> sanitizeSelectedIngredients(Map<String, List<String>> selectedIngredients) {
        Map<String, List<String>> source = selectedIngredients == null ? Map.of() : selectedIngredients;
        Set<String> allowedKeys = Set.of("included", "additional");

        for (String key : source.keySet()) {
            if (!allowedKeys.contains(key)) {
                throw new IllegalArgumentException("selectedIngredients only allows keys: included, additional");
            }
        }

        List<String> included = sanitizeIngredientList(source.get("included"));
        List<String> additional = sanitizeIngredientList(source.get("additional"));

        return Map.of(
            "included", included,
            "additional", additional
        );
    }

    private static List<String> sanitizeIngredientList(List<String> ingredients) {
        if (ingredients == null) {
            return List.of();
        }

        return ingredients.stream()
            .map(ingredient -> validateText(ingredient, "ingredient"))
            .toList();
    }
}

