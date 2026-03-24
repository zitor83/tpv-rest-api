package com.ortiz.tpv_backend.infrastructure.persistence.repository;

import com.ortiz.tpv_backend.infrastructure.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, String> {

	List<ExpenseEntity> findAllByDateBetween(LocalDateTime start, LocalDateTime end);

	List<ExpenseEntity> findAllByDateGreaterThanEqualAndDateLessThan(LocalDateTime startInclusive, LocalDateTime endExclusive);
}

