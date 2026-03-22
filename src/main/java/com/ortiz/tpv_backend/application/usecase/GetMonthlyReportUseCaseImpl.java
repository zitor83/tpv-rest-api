package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.MonthlyReportResponseDTO;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMonthlyReportUseCaseImpl implements GetMonthlyReportUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Override
    public MonthlyReportResponseDTO getByMonth(int year, int month) {
        if (year <= 0) {
            throw new IllegalArgumentException("year must be greater than 0");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        List<Order> orders = orderRepositoryPort.findByDateBetween(start, end);
        List<Expense> expenses = expenseRepositoryPort.findByDateBetween(start, end);

        BigDecimal totalSales = orders.stream()
            .map(Order::getGrandTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MonthlyReportResponseDTO.builder()
            .month(month)
            .year(year)
            .totalSales(totalSales)
            .totalExpenses(totalExpenses)
            .netProfit(totalSales.subtract(totalExpenses))
            .orderCount(orders.size())
            .dailyBreakdown(List.of())
            .build();
    }
}

