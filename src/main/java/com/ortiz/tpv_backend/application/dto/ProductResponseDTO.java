package com.ortiz.tpv_backend.application.dto;

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
public class ProductResponseDTO {

    private String id;
    private String name;
    private BigDecimal price;
    private String category;
    private String description;
    private String imageUrl;
    private Boolean available;
    private List<String> defaultIngredients;
}

