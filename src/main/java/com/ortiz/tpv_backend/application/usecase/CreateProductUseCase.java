package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;

public interface CreateProductUseCase {

    ProductResponseDTO create(ProductRequestDTO requestDTO);
}

