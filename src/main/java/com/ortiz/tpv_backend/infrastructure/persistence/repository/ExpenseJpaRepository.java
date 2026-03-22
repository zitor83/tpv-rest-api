package com.ortiz.tpv_backend.infrastructure.persistence.repository;

import com.ortiz.tpv_backend.infrastructure.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, String> {
}

