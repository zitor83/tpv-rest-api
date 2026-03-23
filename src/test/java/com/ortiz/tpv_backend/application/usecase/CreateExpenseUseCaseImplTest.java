package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateExpenseUseCaseImplTest {

    @Mock
    private ExpenseRepositoryPort expenseRepositoryPort;

    @InjectMocks
    private CreateExpenseUseCaseImpl createExpenseUseCase;

    @Test
    void givenValidRequest_whenCreate_thenPersistExpenseAndReturnResponse() {
        ExpenseRequestDTO request = ExpenseRequestDTO.builder()
            .amount(new BigDecimal("12.50"))
            .description("Compra de servilletas")
            .date(LocalDateTime.parse("2026-03-22T10:30:00"))
            .build();

        Expense savedExpense = new Expense(
            "exp-123",
            new BigDecimal("12.50"),
            "Compra de servilletas",
            LocalDateTime.parse("2026-03-22T10:30:00"),
            LocalDateTime.parse("2026-03-22T10:31:00")
        );

        when(expenseRepositoryPort.save(any(Expense.class))).thenReturn(savedExpense);

        ExpenseResponseDTO result = createExpenseUseCase.create(request);

        assertEquals("exp-123", result.getId());
        assertEquals(new BigDecimal("12.50"), result.getAmount());
        assertEquals("Compra de servilletas", result.getDescription());
        assertEquals(LocalDateTime.parse("2026-03-22T10:30:00"), result.getDate());
        assertEquals(LocalDateTime.parse("2026-03-22T10:31:00"), result.getCreatedAt());
        verify(expenseRepositoryPort).save(any(Expense.class));
    }

    @Test
    void givenNegativeAmount_whenCreate_thenThrowIllegalArgumentExceptionAndDoNotPersist() {
        ExpenseRequestDTO request = ExpenseRequestDTO.builder()
            .amount(new BigDecimal("-1.00"))
            .description("Gasto invalido")
            .date(LocalDateTime.parse("2026-03-22T10:30:00"))
            .build();

        assertThrows(IllegalArgumentException.class, () -> createExpenseUseCase.create(request));

        verify(expenseRepositoryPort, never()).save(any(Expense.class));
    }

    @Test
    void givenNullAmount_whenCreate_thenThrowIllegalArgumentExceptionAndDoNotPersist() {
        ExpenseRequestDTO request = ExpenseRequestDTO.builder()
            .amount(null)
            .description("Gasto invalido")
            .date(LocalDateTime.parse("2026-03-22T10:30:00"))
            .build();

        assertThrows(IllegalArgumentException.class, () -> createExpenseUseCase.create(request));

        verify(expenseRepositoryPort, never()).save(any(Expense.class));
    }

    @Test
    void givenNullRequest_whenCreate_thenThrowNullPointerExceptionAndDoNotPersist() {
        assertThrows(NullPointerException.class, () -> createExpenseUseCase.create(null));

        verifyNoInteractions(expenseRepositoryPort);
    }
}

