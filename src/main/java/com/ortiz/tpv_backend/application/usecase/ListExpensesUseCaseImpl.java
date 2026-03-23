package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ExpenseMapper;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListExpensesUseCaseImpl implements ListExpensesUseCase {

    private final ExpenseRepositoryPort expenseRepositoryPort;
    private final ExpenseMapper expenseMapper;

    @Override
    public List<ExpenseResponseDTO> list() {
        return expenseRepositoryPort.findAll().stream()
            .map(expenseMapper::toResponse)
            .toList();
    }

    @Override
    public List<ExpenseResponseDTO> listByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        return expenseRepositoryPort.findByDateBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX)).stream()
            .map(expenseMapper::toResponse)
            .toList();
    }

    @Override
    public List<ExpenseResponseDTO> listByMonth(int year, int month) {
        if (year <= 0) {
            throw new IllegalArgumentException("year must be greater than 0");
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be between 1 and 12");
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        return expenseRepositoryPort.findByDateBetween(
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
            ).stream()
            .map(expenseMapper::toResponse)
            .toList();
    }
}

