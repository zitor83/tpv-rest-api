package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Expense;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryPort {

    Expense save(Expense expense);

    Optional<Expense> findById(String id);

    List<Expense> findAll();

    List<Expense> findByDate(LocalDate date);

    List<Expense> findByMonthAndYear(int month, int year);

    List<Expense> findByDateBetween(LocalDateTime start, LocalDateTime end);

    void deleteById(String id);
}

