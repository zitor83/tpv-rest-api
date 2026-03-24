package com.ortiz.tpv_backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ExpenseListResultDTO {

    private final List<ExpenseResponseDTO> expenses;
    private final BigDecimal totalExpenses;
    private final int count;

    public boolean hasSummary() {
        return totalExpenses != null;
    }
}

