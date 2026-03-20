package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;

import java.util.List;

public interface ListProductsUseCase {

    List<ProductResponseDTO> list();
}

