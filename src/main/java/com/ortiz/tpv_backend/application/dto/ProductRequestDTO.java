package com.ortiz.tpv_backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    private String id;

    @NotBlank
    private String name;

    @NotNull
    @Positive
    private BigDecimal price;

    @NotBlank
    private String category;

    @NotBlank
    private String description;

    @NotBlank
    private String imageUrl;

    private Boolean available;

    @NotNull
    @Builder.Default
    private List<@NotBlank String> defaultIngredients = new ArrayList<>();
}

