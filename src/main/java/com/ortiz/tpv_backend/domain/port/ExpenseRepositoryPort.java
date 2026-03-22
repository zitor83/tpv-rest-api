package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryPort {

    Expense save(Expense expense);

    Optional<Expense> findById(String id);

    List<Expense> findAll();

    void deleteById(String id);
}

