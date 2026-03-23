package com.ortiz.tpv_backend.domain.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

@Getter
public final class Order {

    private static final Set<String> ALLOWED_STATUS = Set.of("active", "closed", "paid");
    private static final Set<String> ALLOWED_PAYMENT_METHOD = Set.of("cash", "card");

    private final String id;
    private final LocalDateTime createdAt;
    private final List<OrderLine> lines;
    private final BigDecimal totalAmount;
    private final String tableNumber;
    private final boolean isForTakeaway;
    private final String customerName;
    private final String customNotes;
    private final String paymentMethod;
    private final String status;
    private final BigDecimal tip;

    @Default
    public Order(
        String id,
        LocalDateTime createdAt,
        List<OrderLine> lines,
        boolean isForTakeaway,
        String tableNumber,
        String customerName,
        String customNotes,
        String paymentMethod,
        String status,
        BigDecimal tip
    ) {
        this.id = validateId(id);
        this.createdAt = createdAt == null ? LocalDateTime.now(Clock.systemUTC()) : createdAt;
        this.lines = List.copyOf(validateLines(lines));
        this.totalAmount = this.lines.stream()
            .map(OrderLine::getSubTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.isForTakeaway = isForTakeaway;
        this.tableNumber = validateOptionalText(tableNumber, "tableNumber");
        this.customerName = validateOptionalText(customerName, "customerName");
        this.customNotes = validateOptionalText(customNotes, "customNotes");
        this.paymentMethod = validatePaymentMethod(paymentMethod);
        this.status = validateStatus(status);
        this.tip = validateTip(tip);

        validateServiceModeRules(this.isForTakeaway, this.tableNumber);
    }

    public Order(String id, LocalDateTime createdAt, List<OrderLine> lines) {
        this(id, createdAt, lines, false, null, null, null, null, null, null);
    }

    public static Order create(
        List<OrderLine> lines,
        boolean isForTakeaway,
        String tableNumber,
        String customerName,
        String customNotes,
        String paymentMethod,
        String status,
        BigDecimal tip
    ) {
        return new Order(null, null, lines, isForTakeaway, tableNumber, customerName, customNotes, paymentMethod, status, tip);
    }

    public static Order create(List<OrderLine> lines) {
        return new Order(null, null, lines, false, null, null, null, null, null, null);
    }


    public BigDecimal getGrandTotal() {
        return totalAmount.add(tip);
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

    private static List<OrderLine> validateLines(List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("lines cannot be null or empty");
        }

        for (OrderLine line : lines) {
            Objects.requireNonNull(line, "order line is required");
        }

        return lines;
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

    private static String validatePaymentMethod(String paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }

        String normalized = paymentMethod.trim().toLowerCase(Locale.ROOT);
        if (!ALLOWED_PAYMENT_METHOD.contains(normalized)) {
            throw new IllegalArgumentException("paymentMethod must be one of: cash, card");
        }
        return normalized;
    }

    private static String validateStatus(String status) {
        if (status == null) {
            return "active";
        }

        String normalized = status.trim().toLowerCase(Locale.ROOT);
        if (!ALLOWED_STATUS.contains(normalized)) {
            throw new IllegalArgumentException("status must be one of: active, closed, paid");
        }
        return normalized;
    }

    private static BigDecimal validateTip(BigDecimal tip) {
        if (tip == null) {
            return BigDecimal.ZERO;
        }
        if (tip.signum() < 0) {
            throw new IllegalArgumentException("tip must be greater than or equal to 0");
        }
        return tip;
    }

    private static void validateServiceModeRules(boolean isForTakeaway, String tableNumber) {
        if (isForTakeaway && tableNumber != null) {
            throw new IllegalArgumentException("tableNumber cannot be set for takeaway orders");
        }
    }
}


