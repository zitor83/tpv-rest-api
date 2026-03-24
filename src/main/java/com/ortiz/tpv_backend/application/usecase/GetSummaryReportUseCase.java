package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.SummaryReportResponseDTO;

import java.time.LocalDate;

public interface GetSummaryReportUseCase {

    SummaryReportResponseDTO getByDateRange(LocalDate startDate, LocalDate endDate);
}

