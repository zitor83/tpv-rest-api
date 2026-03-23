package com.ortiz.tpv_backend.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Digits;
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
    @Size(min = 3, max = 255)
    private String name;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    private BigDecimal price;

    @NotBlank
    @Size(max = 100)
    private String category;

    @NotBlank
    @Size(min = 10, max = 500)
    private String description;

    @NotBlank
    @Size(max = 500)
    private String imageUrl;

    private Boolean available;

    @NotNull
    @Size(max = 100)
    @Builder.Default
    private List<@NotBlank String> defaultIngredients = new ArrayList<>();
}

