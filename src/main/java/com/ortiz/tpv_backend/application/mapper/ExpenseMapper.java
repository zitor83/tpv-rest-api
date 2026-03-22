package com.ortiz.tpv_backend.application.mapper;

import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.domain.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Expense toDomain(ExpenseRequestDTO requestDTO);

    ExpenseResponseDTO toResponse(Expense expense);
}

