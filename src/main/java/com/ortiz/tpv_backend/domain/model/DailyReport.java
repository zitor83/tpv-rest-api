package com.ortiz.tpv_backend.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public final class DailyReport {

    private final LocalDate date;
    private final BigDecimal totalSales;
    private final BigDecimal totalExpenses;
    private final BigDecimal netProfit;
    private final int orderCount;
    private final int expenseCount;

    public DailyReport(LocalDate date, BigDecimal totalSales, BigDecimal totalExpenses, int orderCount, int expenseCount) {
        this.date = Objects.requireNonNull(date, "date is required");
        this.totalSales = validateAmount(totalSales, "totalSales");
        this.totalExpenses = validateAmount(totalExpenses, "totalExpenses");
        this.orderCount = validateCount(orderCount, "orderCount");
        this.expenseCount = validateCount(expenseCount, "expenseCount");
        this.netProfit = this.totalSales.subtract(this.totalExpenses);
    }

    public LocalDate getDate() {
        return date;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public BigDecimal getNetProfit() {
        return netProfit;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public int getExpenseCount() {
        return expenseCount;
    }

    private static BigDecimal validateAmount(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        if (value.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
        return value;
    }

    private static int validateCount(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " cannot be negative");
        }
        return value;
    }
}

