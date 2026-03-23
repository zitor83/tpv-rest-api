package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @Size(min = 1, max = 100)
    private String tableNumber;

    @NotNull
    private Boolean isForTakeaway;

    @Size(min = 1, max = 255)
    private String customerName;

    @Size(max = 500)
    private String customNotes;

    @Pattern(regexp = "^(cash|card)$", message = "paymentMethod must be 'cash' or 'card'")
    private String paymentMethod;

    @Pattern(regexp = "^(active|closed|paid)$", message = "status must be 'active', 'closed' or 'paid'")
    private String status;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal tip;

    @NotEmpty
    @Size(min = 1)
    @Valid
    @JsonProperty("items")
    @JsonAlias("lines")
    private List<OrderLineRequestDTO> lines;
}

