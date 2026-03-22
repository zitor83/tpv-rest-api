package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.DailyReportResponseDTO;
import com.ortiz.tpv_backend.application.dto.MonthlyReportResponseDTO;
import com.ortiz.tpv_backend.application.usecase.GetDailyReportUseCase;
import com.ortiz.tpv_backend.application.usecase.GetMonthlyReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final GetDailyReportUseCase getDailyReportUseCase;
    private final GetMonthlyReportUseCase getMonthlyReportUseCase;

    @GetMapping("/daily/{date}")
    public ResponseEntity<ApiResponse<DailyReportResponseDTO>> getDailyReport(@PathVariable String date) {
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
    public ResponseEntity<ApiResponse<MonthlyReportResponseDTO>> getMonthlyReport(
        @RequestParam("year") int year,
        @RequestParam("month") int month
    ) {
        MonthlyReportResponseDTO report = getMonthlyReportUseCase.getByMonth(year, month);
        ApiResponse<MonthlyReportResponseDTO> response = ApiResponse.success("Reporte mensual obtenido correctamente", report);

        return ResponseEntity.ok(response);
    }
}

