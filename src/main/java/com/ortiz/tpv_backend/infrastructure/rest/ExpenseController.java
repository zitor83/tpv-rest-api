package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ExpenseMapper;
import com.ortiz.tpv_backend.application.usecase.CreateExpenseUseCase;
import com.ortiz.tpv_backend.application.usecase.DeleteExpenseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final CreateExpenseUseCase createExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;
    private final ExpenseMapper expenseMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> createExpense(@Valid @RequestBody ExpenseRequestDTO requestDTO) {
        ExpenseResponseDTO createdExpense = createExpenseUseCase.create(requestDTO);
        ApiResponse<ExpenseResponseDTO> response = ApiResponse.success("Gasto creado correctamente", createdExpense);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
        deleteExpenseUseCase.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success("Gasto eliminado correctamente", null);

        return ResponseEntity.ok(response);
    }
}

