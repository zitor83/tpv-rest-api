package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.mapper.OrderMapper;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListOrdersUseCaseImpl implements ListOrdersUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponseDTO> list() {
        return orderMapper.toResponseList(orderRepositoryPort.findAll());
    }
}

