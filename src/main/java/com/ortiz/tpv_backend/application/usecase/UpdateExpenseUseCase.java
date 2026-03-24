package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseUpdateRequestDTO;

public interface UpdateExpenseUseCase {

    ExpenseResponseDTO update(String id, ExpenseUpdateRequestDTO requestDTO);
}

