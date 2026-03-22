package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.DailyReportResponseDTO;

import java.time.LocalDate;

public interface GetDailyReportUseCase {

    DailyReportResponseDTO getByDate(LocalDate date);
}

