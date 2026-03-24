package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseUpdateRequestDTO;
import com.ortiz.tpv_backend.application.exception.ResourceNotFoundException;
import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateExpenseUseCaseImpl implements UpdateExpenseUseCase {

    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Override
    public ExpenseResponseDTO update(String id, ExpenseUpdateRequestDTO requestDTO) {
        String safeId = normalizeId(id);
        ExpenseUpdateRequestDTO safeRequest = Objects.requireNonNull(requestDTO, "requestDTO is required");

        Expense currentExpense = expenseRepositoryPort.findById(safeId)
            .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + safeId));

        Expense expenseToUpdate = new Expense(
            currentExpense.getId(),
            safeRequest.getAmount(),
            safeRequest.getDescription(),
            currentExpense.getDate(),
            currentExpense.getCreatedAt()
        );

        Expense updatedExpense = expenseRepositoryPort.save(expenseToUpdate);
        return ExpenseResponseDTO.builder()
            .id(updatedExpense.getId())
            .amount(updatedExpense.getAmount())
            .description(updatedExpense.getDescription())
            .date(updatedExpense.getDate())
            .createdAt(updatedExpense.getCreatedAt())
            .build();
    }

    private static String normalizeId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }
        return id.trim();
    }
}

