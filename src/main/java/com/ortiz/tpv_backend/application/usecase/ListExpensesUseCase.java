package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseListResultDTO;

import java.time.LocalDate;

public interface ListExpensesUseCase {

    ExpenseListResultDTO list(LocalDate date, Integer month, Integer year);
}

