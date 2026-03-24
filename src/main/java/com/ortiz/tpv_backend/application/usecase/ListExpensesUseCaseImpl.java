package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseListResultDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ExpenseMapper;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListExpensesUseCaseImpl implements ListExpensesUseCase {

    private final ExpenseRepositoryPort expenseRepositoryPort;
    private final ExpenseMapper expenseMapper;

    @Override
    public ExpenseListResultDTO list(LocalDate date, Integer month, Integer year) {
        List<Expense> expenses;
        boolean filtered;

        if (date != null) {
            if (month != null || year != null) {
                throw new IllegalArgumentException("date cannot be combined with month/year");
            }
            expenses = expenseRepositoryPort.findByDate(date);
            filtered = true;
        } else if (month != null || year != null) {
            if (month == null || year == null) {
                throw new IllegalArgumentException("month and year must be provided together");
            }
            expenses = expenseRepositoryPort.findByMonthAndYear(month, year);
            filtered = true;
        } else {
            expenses = expenseRepositoryPort.findAll();
            filtered = false;
        }

        List<ExpenseResponseDTO> responseItems = expenses.stream()
            .map(expenseMapper::toResponse)
            .toList();

        if (!filtered) {
            return ExpenseListResultDTO.builder()
                .expenses(responseItems)
                .totalExpenses(null)
                .count(responseItems.size())
                .build();
        }

        BigDecimal total = responseItems.stream()
            .map(item -> item.getAmount() == null ? BigDecimal.ZERO : item.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ExpenseListResultDTO.builder()
            .expenses(responseItems)
            .totalExpenses(total)
            .count(responseItems.size())
            .build();
    }
}

