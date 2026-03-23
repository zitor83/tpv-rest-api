package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String productId;

    @Size(max = 255)
    private String productName;

    @JsonProperty("price")
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal unitPrice;

    @JsonProperty("subtotal")
    @JsonAlias("subTotal")
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal subTotal;

    @NotNull
    @Positive
    private Integer quantity;

    @Size(max = 255)
    private String notes;

    @Valid
    private SelectedIngredientsDTO selectedIngredients;
}

