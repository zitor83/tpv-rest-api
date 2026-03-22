package com.ortiz.tpv_backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyReportResponseDTO {

    private int month;
    private int year;
    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    private int orderCount;
    private List<DailyReportResponseDTO> dailyBreakdown;
}

