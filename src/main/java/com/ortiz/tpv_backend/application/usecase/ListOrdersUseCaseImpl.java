package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.mapper.OrderMapper;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
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

    @Override
    public List<OrderResponseDTO> listByDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("date is required");
        }
        return orderMapper.toResponseList(
            orderRepositoryPort.findByDateBetween(date.atStartOfDay(), date.atTime(LocalTime.MAX))
        );
    }

    @Override
    public List<OrderResponseDTO> list(LocalDate date, String status) {
        String normalizedStatus = normalizeStatus(status);

        if (date != null && normalizedStatus != null) {
            return orderMapper.toResponseList(
                orderRepositoryPort.findByDateBetweenAndStatus(date.atStartOfDay(), date.atTime(LocalTime.MAX), normalizedStatus)
            );
        }
        if (date != null) {
            return listByDate(date);
        }
        if (normalizedStatus != null) {
            return orderMapper.toResponseList(orderRepositoryPort.findByStatus(normalizedStatus));
        }

        return list();
    }

    private static String normalizeStatus(String status) {
        if (status == null) {
            return null;
        }
        String normalized = status.trim();
        return normalized.isEmpty() ? null : normalized;
    }
}

