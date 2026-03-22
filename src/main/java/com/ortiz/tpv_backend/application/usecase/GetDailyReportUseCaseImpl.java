package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.DailyReportResponseDTO;
import com.ortiz.tpv_backend.domain.model.DailyReport;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetDailyReportUseCaseImpl implements GetDailyReportUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Override
    public DailyReportResponseDTO getByDate(LocalDate date) {
        LocalDate safeDate = Objects.requireNonNull(date, "date is required");

        LocalDateTime start = safeDate.atStartOfDay();
        LocalDateTime end = safeDate.atTime(LocalTime.MAX);

        List<Order> orders = orderRepositoryPort.findByDateBetween(start, end);
        List<Expense> expenses = expenseRepositoryPort.findByDateBetween(start, end);

        BigDecimal totalSales = orders.stream()
            .map(Order::getGrandTotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenses.stream()
            .map(Expense::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        DailyReport dailyReport = new DailyReport(
            safeDate,
            totalSales,
            totalExpenses,
            orders.size(),
            expenses.size()
        );

        return DailyReportResponseDTO.builder()
            .date(dailyReport.getDate())
            .totalSales(dailyReport.getTotalSales())
            .totalExpenses(dailyReport.getTotalExpenses())
            .netProfit(dailyReport.getNetProfit())
            .orderCount(dailyReport.getOrderCount())
            .expenseCount(dailyReport.getExpenseCount())
            .build();
    }
}

