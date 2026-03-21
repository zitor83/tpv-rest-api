package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;

import java.util.List;

public interface ListOrdersUseCase {

    List<OrderResponseDTO> list();
}

