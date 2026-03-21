package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAll();
}

