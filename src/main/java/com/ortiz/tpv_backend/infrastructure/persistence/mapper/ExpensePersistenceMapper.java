package com.ortiz.tpv_backend.infrastructure.persistence.mapper;

import com.ortiz.tpv_backend.domain.model.Expense;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.ExpenseEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExpensePersistenceMapper {

    ExpenseEntity toEntity(Expense expense);

    Expense toDomain(ExpenseEntity entity);

    List<Expense> toDomainList(List<ExpenseEntity> entities);
}

