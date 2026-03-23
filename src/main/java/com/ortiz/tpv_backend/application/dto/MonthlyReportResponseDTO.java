package com.ortiz.tpv_backend.application.dto;

import lombok.*;

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

