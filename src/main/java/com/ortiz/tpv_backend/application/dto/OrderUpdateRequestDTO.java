package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class OrderUpdateRequestDTO {

    @Valid
    @JsonProperty("items")
    @JsonAlias("lines")
    private List<OrderLineRequestDTO> items;

    @Size(min = 1, max = 100)
    private String tableNumber;

    private Boolean isForTakeaway;

    @Size(min = 1, max = 255)
    private String customerName;

    @Pattern(regexp = "^(active|closed|paid)$", message = "status must be 'active', 'closed' or 'paid'")
    private String status;

    @Pattern(regexp = "^(cash|card)$", message = "paymentMethod must be 'cash' or 'card'")
    private String paymentMethod;

    @DecimalMin("0.0")
    @Digits(integer = 10, fraction = 2)
    private BigDecimal tip;

    @Size(max = 500)
    private String customNotes;
}


