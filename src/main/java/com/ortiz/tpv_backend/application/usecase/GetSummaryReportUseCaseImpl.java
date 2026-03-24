package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.SummaryReportResponseDTO;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetSummaryReportUseCaseImpl implements GetSummaryReportUseCase {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    private final OrderRepositoryPort orderRepositoryPort;
    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Override
    public SummaryReportResponseDTO getByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDate safeStartDate = Objects.requireNonNull(startDate, "startDate is required");
        LocalDate safeEndDate = Objects.requireNonNull(endDate, "endDate is required");
        if (safeEndDate.isBefore(safeStartDate)) {
            throw new IllegalArgumentException("endDate must be greater than or equal to startDate");
        }

        LocalDateTime start = safeStartDate.atStartOfDay();
        LocalDateTime end = safeEndDate.atTime(LocalTime.MAX);

        List<Order> orders = orderRepositoryPort.findByDateBetween(start, end);
        List<Expense> expenses = expenseRepositoryPort.findByDateBetween(start, end);

        BigDecimal totalSales = scale(orders.stream()
            .map(Order::getGrandTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal totalExpenses = scale(expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal averageOrderValue = orders.isEmpty()
            ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            : scale(totalSales.divide(BigDecimal.valueOf(orders.size()), 2, RoundingMode.HALF_UP));

        BigDecimal averageExpense = expenses.isEmpty()
            ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            : scale(totalExpenses.divide(BigDecimal.valueOf(expenses.size()), 2, RoundingMode.HALF_UP));

        BigDecimal cashTotal = scale(orders.stream()
            .filter(order -> "cash".equals(order.getPaymentMethod()))
            .map(Order::getGrandTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal cardTotal = scale(orders.stream()
            .filter(order -> "card".equals(order.getPaymentMethod()))
            .map(Order::getGrandTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add));

        BigDecimal netProfit = scale(totalSales.subtract(totalExpenses));

        BigDecimal profitMargin = totalSales.signum() == 0
            ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
            : scale(netProfit.multiply(ONE_HUNDRED).divide(totalSales, 2, RoundingMode.HALF_UP));

        long days = ChronoUnit.DAYS.between(safeStartDate, safeEndDate) + 1;

        Map<String, BigDecimal> paymentMethods = new LinkedHashMap<>();
        paymentMethods.put("cash", cashTotal);
        paymentMethods.put("card", cardTotal);

        return SummaryReportResponseDTO.builder()
            .period(SummaryReportResponseDTO.PeriodInfo.builder()
                .startDate(safeStartDate)
                .endDate(safeEndDate)
                .days(Math.toIntExact(days))
                .build())
            .sales(SummaryReportResponseDTO.SalesInfo.builder()
                .totalSales(totalSales)
                .orderCount(orders.size())
                .averageOrderValue(averageOrderValue)
                .paymentMethods(paymentMethods)
                .build())
            .expenses(SummaryReportResponseDTO.ExpensesInfo.builder()
                .totalExpenses(totalExpenses)
                .expenseCount(expenses.size())
                .averageExpense(averageExpense)
                .build())
            .profit(SummaryReportResponseDTO.ProfitInfo.builder()
                .netProfit(netProfit)
                .profitMargin(profitMargin)
                .build())
            .build();
    }

    private static BigDecimal scale(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}

