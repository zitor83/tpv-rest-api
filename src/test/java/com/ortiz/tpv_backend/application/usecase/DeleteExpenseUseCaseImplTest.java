package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteExpenseUseCaseImplTest {

    @Mock
    private ExpenseRepositoryPort expenseRepositoryPort;

    @InjectMocks
    private DeleteExpenseUseCaseImpl deleteExpenseUseCase;

    @Test
    void givenValidId_whenDelete_thenCallRepositoryDeleteById() {
        deleteExpenseUseCase.deleteById("exp-001");

        verify(expenseRepositoryPort).deleteById("exp-001");
    }

    @Test
    void givenIdWithSpaces_whenDelete_thenTrimAndCallRepositoryDeleteById() {
        deleteExpenseUseCase.deleteById("  exp-002  ");

        verify(expenseRepositoryPort).deleteById("exp-002");
    }

    @Test
    void givenNullId_whenDelete_thenThrowIllegalArgumentExceptionAndDoNotCallRepository() {
        assertThrows(IllegalArgumentException.class, () -> deleteExpenseUseCase.deleteById(null));

        verify(expenseRepositoryPort, never()).deleteById("exp-003");
    }

    @Test
    void givenBlankId_whenDelete_thenThrowIllegalArgumentExceptionAndDoNotCallRepository() {
        assertThrows(IllegalArgumentException.class, () -> deleteExpenseUseCase.deleteById("   "));

        verify(expenseRepositoryPort, never()).deleteById("exp-004");
    }
}

