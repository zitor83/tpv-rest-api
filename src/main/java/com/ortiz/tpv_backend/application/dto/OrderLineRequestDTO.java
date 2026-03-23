package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineRequestDTO {

    @NotBlank
    private String productId;

    private String productName;

    @JsonProperty("price")
    private BigDecimal unitPrice;

    @JsonProperty("subtotal")
    @JsonAlias("subTotal")
    private BigDecimal subTotal;

    @NotNull
    @Positive
    private Integer quantity;

    private String notes;

    private SelectedIngredientsDTO selectedIngredients;
}

