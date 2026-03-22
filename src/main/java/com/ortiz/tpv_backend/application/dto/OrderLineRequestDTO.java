package com.ortiz.tpv_backend.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineRequestDTO {

    @NotBlank
    private String productId;

    @NotNull
    @Positive
    private Integer quantity;

    private String notes;

    private Map<String, List<String>> selectedIngredients;
}

