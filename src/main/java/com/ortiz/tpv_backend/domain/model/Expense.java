package com.ortiz.tpv_backend.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public final class Expense {

    private final String id;
    private final BigDecimal amount;
    private final String description;
    private final LocalDateTime date;
    private final LocalDateTime createdAt;

    public Expense(String id, BigDecimal amount, String description, LocalDateTime date, LocalDateTime createdAt) {
        this.id = validateId(id);
        this.amount = validateAmount(amount);
        this.description = validateDescription(description);
        this.date = Objects.requireNonNull(date, "date is required");
        this.createdAt = createdAt == null ? LocalDateTime.now(Clock.systemUTC()) : createdAt;
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

    private static BigDecimal validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("amount is required");
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be greater than 0");
        }
        return amount;
    }

    private static String validateDescription(String description) {
        if (description == null) {
            throw new IllegalArgumentException("description is required");
        }

        String normalized = description.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("description cannot be blank");
        }
        return normalized;
    }
}

