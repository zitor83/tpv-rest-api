package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.ExpenseListResultDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseRequestDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseResponseDTO;
import com.ortiz.tpv_backend.application.dto.ExpenseUpdateRequestDTO;
import com.ortiz.tpv_backend.application.usecase.CreateExpenseUseCase;
import com.ortiz.tpv_backend.application.usecase.DeleteExpenseUseCase;
import com.ortiz.tpv_backend.application.usecase.ListExpensesUseCase;
import com.ortiz.tpv_backend.application.usecase.UpdateExpenseUseCase;
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

import java.time.LocalDate;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
@Tag(name = "Expenses", description = "Gestiona gastos operativos del negocio, incluyendo alta, consulta filtrada y eliminacion.")
public class ExpenseController {

    private final CreateExpenseUseCase createExpenseUseCase;
    private final DeleteExpenseUseCase deleteExpenseUseCase;
    private final ListExpensesUseCase listExpensesUseCase;
    private final UpdateExpenseUseCase updateExpenseUseCase;

    @PostMapping
    @Operation(
        summary = "Registrar nuevo gasto",
        description = "Registra un gasto operativo con fecha, monto y descripcion. El endpoint valida datos de entrada y devuelve el gasto persistido con su identificador.",
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
        description = "Obtiene gastos y admite filtros por date o por combinacion month/year. La respuesta incluye una seccion summary con el total acumulado y cantidad de registros.",
        tags = {"Expenses"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Gastos obtenidos correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parametros invalidos: month y year deben enviarse juntos",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<List<ExpenseResponseDTO>>> listExpenses(
        @RequestParam(required = false) LocalDate date,
        @RequestParam(required = false) Integer month,
        @RequestParam(required = false) Integer year
    ) {
        ExpenseListResultDTO result = listExpensesUseCase.list(date, month, year);

        ApiResponse<List<ExpenseResponseDTO>> response;
        if (result.hasSummary()) {
            Map<String, Object> meta = new LinkedHashMap<>();
            meta.put("totalExpenses", result.getTotalExpenses());
            meta.put("count", result.getCount());
            if (month != null && year != null) {
                meta.put("month", month);
                meta.put("year", year);
            }
            response = ApiResponse.success("Gastos obtenidos correctamente", result.getExpenses(), meta);
        } else {
            response = ApiResponse.success("Gastos obtenidos correctamente", result.getExpenses());
        }

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar gasto",
        description = "Actualiza monto y descripcion de un gasto existente identificado por su ID.",
        tags = {"Expenses"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Gasto actualizado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validación fallida",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Gasto no encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<ExpenseResponseDTO>> updateExpense(
        @PathVariable String id,
        @Valid @RequestBody ExpenseUpdateRequestDTO requestDTO
    ) {
        ExpenseResponseDTO updatedExpense = updateExpenseUseCase.update(id, requestDTO);
        ApiResponse<ExpenseResponseDTO> response = ApiResponse.success("Gasto actualizado correctamente", updatedExpense);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar gasto",
        description = "Elimina un gasto por su identificador de forma permanente. La operacion se usa para correcciones operativas y no admite recuperacion.",
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

