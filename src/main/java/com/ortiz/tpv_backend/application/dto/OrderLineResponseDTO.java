package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineResponseDTO {

    private String productId;
    private String productName;
    private Integer quantity;
    @JsonProperty("price")
    private BigDecimal unitPrice;
    @JsonProperty("subtotal")
    private BigDecimal subTotal;
    private String notes;
    private SelectedIngredientsDTO selectedIngredients;
}

