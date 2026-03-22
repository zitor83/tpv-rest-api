package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeleteExpenseUseCaseImpl implements DeleteExpenseUseCase {

    private final ExpenseRepositoryPort expenseRepositoryPort;

    @Override
    public void deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }

        expenseRepositoryPort.deleteById(id.trim());
    }
}


