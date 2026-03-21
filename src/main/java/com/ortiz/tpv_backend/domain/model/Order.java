package com.ortiz.tpv_backend.domain.model;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public final class Order {

    private final Long id;
    private final LocalDateTime createdAt;
    private final List<OrderLine> lines;
    private final BigDecimal totalAmount;

    public Order(Long id, LocalDateTime createdAt, List<OrderLine> lines) {
        this.id = validateId(id);
        this.createdAt = createdAt == null ? LocalDateTime.now(Clock.systemUTC()) : createdAt;
        this.lines = List.copyOf(validateLines(lines));
        this.totalAmount = this.lines.stream()
            .map(OrderLine::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static Order create(List<OrderLine> lines) {
        return new Order(null, null, lines);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<OrderLine> getLines() {
        return lines;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    private static Long validateId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        return id;
    }

    private static List<OrderLine> validateLines(List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines cannot be null or empty");
        }

        for (OrderLine line : lines) {
            Objects.requireNonNull(line, "order line is required");
        }

        return lines;
    }
}


