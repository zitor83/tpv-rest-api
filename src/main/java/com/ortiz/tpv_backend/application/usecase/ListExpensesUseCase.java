package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;

import java.time.LocalDate;
import java.util.List;

public interface ListExpensesUseCase {

    List<ExpenseResponseDTO> list();

    List<ExpenseResponseDTO> listByDate(LocalDate date);

    List<ExpenseResponseDTO> listByMonth(int year, int month);
}

