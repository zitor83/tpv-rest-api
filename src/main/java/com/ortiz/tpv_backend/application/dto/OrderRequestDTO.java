package com.ortiz.tpv_backend.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
public class OrderRequestDTO {

    private String tableNumber;

    @NotNull
    private Boolean isForTakeaway;

    private String customerName;

    private String customNotes;

    @Pattern(regexp = "^(cash|card)$", message = "paymentMethod must be 'cash' or 'card'")
    private String paymentMethod;

    @Pattern(regexp = "^(active|closed|paid)$", message = "status must be 'active', 'closed' or 'paid'")
    private String status;

    @DecimalMin(value = "0.0", inclusive = true)
    private BigDecimal tip;

    @NotEmpty
    @Valid
    private List<OrderLineRequestDTO> lines;
}

