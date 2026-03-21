package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;

public interface GetOrderUseCase {

    OrderResponseDTO getById(Long id);
}

