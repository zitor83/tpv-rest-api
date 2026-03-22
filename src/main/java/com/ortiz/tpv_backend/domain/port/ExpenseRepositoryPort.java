package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Expense;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryPort {

    Expense save(Expense expense);

    Optional<Expense> findById(String id);

    List<Expense> findAll();

    default List<Expense> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end are required");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end must be greater than or equal to start");
        }

        return findAll().stream()
            .filter(expense -> !expense.getDate().isBefore(start) && !expense.getDate().isAfter(end))
            .toList();
    }

    void deleteById(String id);
}

