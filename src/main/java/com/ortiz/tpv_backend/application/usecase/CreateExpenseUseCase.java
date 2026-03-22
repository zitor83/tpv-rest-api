package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;

public interface CreateExpenseUseCase {

    ExpenseResponseDTO create(ExpenseRequestDTO requestDTO);
}

