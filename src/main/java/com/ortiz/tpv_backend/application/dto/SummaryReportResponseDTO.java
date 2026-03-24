package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class SummaryReportResponseDTO {

    private final PeriodInfo period;
    private final SalesInfo sales;
    private final ExpensesInfo expenses;
    private final ProfitInfo profit;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class PeriodInfo {

        @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate startDate;

        @JsonFormat(pattern = "yyyy-MM-dd")
        private final LocalDate endDate;

        private final int days;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SalesInfo {

        private final BigDecimal totalSales;
        private final int orderCount;
        private final BigDecimal averageOrderValue;
        private final Map<String, BigDecimal> paymentMethods;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ExpensesInfo {

        private final BigDecimal totalExpenses;
        private final int expenseCount;
        private final BigDecimal averageExpense;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfitInfo {

        private final BigDecimal netProfit;
        private final BigDecimal profitMargin;
    }
}

