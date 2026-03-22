package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.DailyReportResponseDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetDailyReportUseCaseTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private ExpenseRepositoryPort expenseRepositoryPort;

    @InjectMocks
    private GetDailyReportUseCaseImpl getDailyReportUseCase;

    @Test
    void givenOrdersAndExpensesForDate_whenGetByDate_thenReturnCalculatedDailyReport() {
        LocalDate targetDate = LocalDate.parse("2026-03-22");

        Order orderOne = new Order(
            "ord-1",
            LocalDateTime.parse("2026-03-22T10:00:00"),
            List.of(new OrderLine(1L, "prod-1", "Menu A", new BigDecimal("20.00"), 1)),
            false,
            "Mesa 1",
            null,
            null,
            "card",
            "paid",
            BigDecimal.ZERO
        );

        Order orderTwo = new Order(
            "ord-2",
            LocalDateTime.parse("2026-03-22T11:00:00"),
            List.of(new OrderLine(2L, "prod-2", "Menu B", new BigDecimal("30.50"), 1)),
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
            "Compra de bebidas",
            LocalDateTime.parse("2026-03-22T09:30:00"),
            LocalDateTime.parse("2026-03-22T09:31:00")
        );

        when(orderRepositoryPort.findByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(orderOne, orderTwo));
        when(expenseRepositoryPort.findByDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
            .thenReturn(List.of(expense));

        DailyReportResponseDTO result = getDailyReportUseCase.getByDate(targetDate);

        assertEquals(targetDate, result.getDate());
        assertEquals(new BigDecimal("50.50"), result.getTotalSales());
        assertEquals(new BigDecimal("15.00"), result.getTotalExpenses());
        assertEquals(new BigDecimal("35.50"), result.getNetProfit());
        assertEquals(Integer.valueOf(2), result.getOrderCount());
        assertEquals(Integer.valueOf(1), result.getExpenseCount());
    }
}

