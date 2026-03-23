package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.DailyReportResponseDTO;
import com.ortiz.tpv_backend.application.dto.MonthlyReportResponseDTO;
import com.ortiz.tpv_backend.application.usecase.GetDailyReportUseCase;
import com.ortiz.tpv_backend.application.usecase.GetMonthlyReportUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "Financial Reports", description = "Expone reportes financieros consolidados para analisis diario y mensual del TPV.")
public class ReportController {

    private final GetDailyReportUseCase getDailyReportUseCase;
    private final GetMonthlyReportUseCase getMonthlyReportUseCase;

    @GetMapping("/daily/{date}")
    @Operation(
        summary = "Obtener reporte diario",
        description = "Genera el reporte financiero de una fecha (YYYY-MM-DD) con ventas, gastos y utilidad neta del dia para cierre operativo.",
        tags = {"Financial Reports"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reporte diario obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Formato de fecha inválido. Debe ser YYYY-MM-DD",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado. Se requiere token JWT válido",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "No autorizado para acceder a este recurso",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<DailyReportResponseDTO>> getDailyReport(
        @PathVariable String date
    ) {
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("date must use format YYYY-MM-DD");
        }

        DailyReportResponseDTO report = getDailyReportUseCase.getByDate(parsedDate);
        ApiResponse<DailyReportResponseDTO> response = ApiResponse.success("Reporte diario obtenido correctamente", report);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly")
    @Operation(
        summary = "Obtener reporte mensual",
        description = "Genera el reporte financiero de un mes usando year y month como filtros obligatorios. Incluye totales mensuales y desglose diario para analitica.",
        tags = {"Financial Reports"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Reporte mensual obtenido exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Parámetros inválidos (año o mes fuera de rango)",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado. Se requiere token JWT válido",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "No autorizado para acceder a este recurso",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<MonthlyReportResponseDTO>> getMonthlyReport(
        @RequestParam("year") int year,
        @RequestParam("month") int month
    ) {
        MonthlyReportResponseDTO report = getMonthlyReportUseCase.getByMonth(year, month);
        ApiResponse<MonthlyReportResponseDTO> response = ApiResponse.success("Reporte mensual obtenido correctamente", report);

        return ResponseEntity.ok(response);
    }
}

