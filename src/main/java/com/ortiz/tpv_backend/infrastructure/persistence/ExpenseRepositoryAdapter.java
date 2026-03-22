package com.ortiz.tpv_backend.infrastructure.persistence;

import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.domain.port.ExpenseRepositoryPort;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.ExpenseEntity;
import com.ortiz.tpv_backend.infrastructure.persistence.mapper.ExpensePersistenceMapper;
import com.ortiz.tpv_backend.infrastructure.persistence.repository.ExpenseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryAdapter implements ExpenseRepositoryPort {

    private final ExpenseJpaRepository expenseJpaRepository;
    private final ExpensePersistenceMapper expensePersistenceMapper;

    @Override
    public Expense save(Expense expense) {
        Expense safeExpense = Objects.requireNonNull(expense, "expense is required");

        ExpenseEntity entityToSave = expensePersistenceMapper.toEntity(safeExpense);
        ExpenseEntity savedEntity = expenseJpaRepository.save(entityToSave);

        return expensePersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Expense> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }

        return expenseJpaRepository.findById(id.trim())
            .map(expensePersistenceMapper::toDomain);
    }

    @Override
    public List<Expense> findAll() {
        return expensePersistenceMapper.toDomainList(expenseJpaRepository.findAll());
    }

    @Override
    public void deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }

        expenseJpaRepository.deleteById(id.trim());
    }
}

