package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(String id);

    List<Order> findAll();

    default List<Order> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("start and end are required");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end must be greater than or equal to start");
        }

        return findAll().stream()
            .filter(order -> !order.getCreatedAt().isBefore(start) && !order.getCreatedAt().isAfter(end))
            .toList();
    }
}

