package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.MonthlyReportResponseDTO;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMonthlyReportUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private ExpenseRepositoryPort expenseRepositoryPort;

    @InjectMocks
    private GetMonthlyReportUseCaseImpl getMonthlyReportUseCase;

    @Test
    void givenOrdersAndExpensesInMonth_whenGetByMonth_thenReturnAggregatedTotals() {
        Order firstOrder = new Order(
            "ord-1",
            LocalDateTime.parse("2026-03-05T12:00:00"),
            List.of(new OrderLine(1L, "prod-1", "Menu 1", new BigDecimal("20.00"), 1)),
            false,
            "Mesa 1",
            null,
            null,
            "card",
            "paid",
            BigDecimal.ZERO
        );

        Order secondOrder = new Order(
            "ord-2",
            LocalDateTime.parse("2026-03-10T14:00:00"),
            List.of(new OrderLine(2L, "prod-2", "Menu 2", new BigDecimal("30.50"), 1)),
            false,
            "Mesa 2",
            null,
            null,
            "cash",
            "paid",
            BigDecimal.ZERO
        );

        Expense expense = new Expense(
            "exp-1",
            new BigDecimal("15.00"),
            "Proveedor",
            LocalDateTime.parse("2026-03-08T09:00:00"),
            LocalDateTime.parse("2026-03-08T09:01:00")
        );

        when(orderRepositoryPort.findByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(firstOrder, secondOrder));
        when(expenseRepositoryPort.findByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(expense));

        MonthlyReportResponseDTO result = getMonthlyReportUseCase.getByMonth(2026, 3);

        assertEquals(3, result.getMonth());
        assertEquals(2026, result.getYear());
        assertEquals(new BigDecimal("50.50"), result.getTotalSales());
        assertEquals(new BigDecimal("15.00"), result.getTotalExpenses());
        assertEquals(new BigDecimal("35.50"), result.getNetProfit());
        assertEquals(2, result.getOrderCount());
    }
}

