package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;

public interface CreateOrderUseCase {

    OrderResponseDTO create(OrderRequestDTO requestDTO);
}

