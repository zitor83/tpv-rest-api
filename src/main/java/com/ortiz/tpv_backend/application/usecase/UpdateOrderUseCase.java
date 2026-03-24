package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.dto.OrderUpdateRequestDTO;

public interface UpdateOrderUseCase {

    OrderResponseDTO update(String id, OrderUpdateRequestDTO requestDTO);
}

