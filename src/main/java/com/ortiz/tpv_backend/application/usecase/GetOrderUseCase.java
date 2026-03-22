package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;

public interface GetOrderUseCase {

    OrderResponseDTO getById(String id);

    default OrderResponseDTO getById(Long id) {
        return getById(id == null ? null : id.toString());
    }
}

