package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.MonthlyReportResponseDTO;

public interface GetMonthlyReportUseCase {

    MonthlyReportResponseDTO getByMonth(int year, int month);
}

