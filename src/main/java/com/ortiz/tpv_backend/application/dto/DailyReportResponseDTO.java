package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyReportResponseDTO {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private BigDecimal totalSales;
    private BigDecimal totalExpenses;
    private BigDecimal netProfit;
    private Integer orderCount;
    private Integer expenseCount;
}

