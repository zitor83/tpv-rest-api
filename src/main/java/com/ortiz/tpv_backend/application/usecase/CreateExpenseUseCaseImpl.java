package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import lombok.RequiredArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor
public class CreateExpenseUseCaseImpl implements CreateExpenseUseCase {

    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Override
    public ExpenseResponseDTO create(ExpenseRequestDTO requestDTO) {
        ExpenseRequestDTO safeRequest = Objects.requireNonNull(requestDTO, "requestDTO is required");

        Expense expenseToCreate = new Expense(
            UUID.randomUUID().toString(),
            safeRequest.getAmount(),
            safeRequest.getDescription(),
            safeRequest.getDate(),
            null
        );

        Expense createdExpense = expenseRepositoryPort.save(expenseToCreate);
        return toResponse(createdExpense);
    }

    private static ExpenseResponseDTO toResponse(Expense expense) {
        return ExpenseResponseDTO.builder()
            .id(expense.getId())
            .amount(expense.getAmount())
            .description(expense.getDescription())
            .date(expense.getDate())
            .createdAt(expense.getCreatedAt())
            .build();
    }
}


