package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.exception.ResourceNotFoundException;
import com.ortiz.tpv_backend.application.mapper.OrderMapper;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetOrderUseCaseImpl implements GetOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO getById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }

        Order order = orderRepositoryPort.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con id: " + id));

        return orderMapper.toResponse(order);
    }
}

