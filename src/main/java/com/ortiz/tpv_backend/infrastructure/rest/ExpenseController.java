package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.usecase.CreateExpenseUseCase;
import com.ortiz.tpv_backend.application.usecase.DeleteExpenseUseCase;
import com.ortiz.tpv_backend.application.usecase.ListExpensesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Endpoints para registrar y gestionar gastos operacionales del TPV")
public class ExpenseController {

    private final CreateExpenseUseCase createExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;
    private final ListExpensesUseCase listExpensesUseCase;

    @PostMapping
    @Operation(
        summary = "Registrar nuevo gasto",
        description = "Crea un nuevo registro de gasto operacional (suministros, mantenimiento, etc.) con fecha, monto y descripción.",
        tags = {"Expenses"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Gasto registrado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validación fallida: monto negativo o descripción vacía",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> createExpense(@Valid @RequestBody ExpenseRequestDTO requestDTO) {
        ExpenseResponseDTO createdExpense = createExpenseUseCase.create(requestDTO);
        ApiResponse<ExpenseResponseDTO> response = ApiResponse.success("Gasto creado correctamente", createdExpense);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar gastos",
        description = "Obtiene gastos con filtros opcionales por fecha o por mes/año.",
        tags = {"Expenses"}
    )
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<List<ExpenseResponseDTO>>> listExpenses(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        List<ExpenseResponseDTO> expenses;

        if (date != null) {
            expenses = listExpensesUseCase.listByDate(date);
        } else if (month != null && year != null) {
            expenses = listExpensesUseCase.listByMonth(year, month);
        } else if (month != null || year != null) {
            throw new IllegalArgumentException("month and year must be provided together");
        } else {
            expenses = listExpensesUseCase.list();
        }

        BigDecimal total = expenses.stream()
            .map(expense -> expense.getAmount() == null ? BigDecimal.ZERO : expense.getAmount())
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> summary = new java.util.LinkedHashMap<>();
        summary.put("count", expenses.size());
        summary.put("totalExpenses", total);
        if (month != null && year != null) {
            summary.put("month", month);
            summary.put("year", year);
        }

        ApiResponse<List<ExpenseResponseDTO>> response = ApiResponse.success(
            "Gastos obtenidos correctamente",
            expenses,
            Map.of("summary", summary)
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar gasto",
        description = "Elimina un registro de gasto por su identificador. Esta operación es permanente.",
        tags = {"Expenses"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Gasto eliminado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Gasto con el ID especificado no existe",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<Void>> deleteExpense(@PathVariable String id) {
        deleteExpenseUseCase.deleteById(id);
        ApiResponse<Void> response = ApiResponse.success("Gasto eliminado correctamente", null);

        return ResponseEntity.ok(response);
    }
}

