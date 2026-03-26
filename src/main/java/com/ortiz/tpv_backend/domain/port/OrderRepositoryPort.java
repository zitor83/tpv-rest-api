package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(String id);

    List<Order> findAll();

    List<Order> findByStatus(String status);

    List<Order> findByDateBetween(LocalDateTime start, LocalDateTime end);

    List<Order> findByDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status);
}

