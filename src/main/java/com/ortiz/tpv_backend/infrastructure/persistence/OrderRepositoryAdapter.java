package com.ortiz.tpv_backend.infrastructure.persistence;

import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.OrderEntity;
import com.ortiz.tpv_backend.infrastructure.persistence.mapper.OrderPersistenceMapper;
import com.ortiz.tpv_backend.infrastructure.persistence.repository.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderPersistenceMapper orderPersistenceMapper;

    @Override
    public Order save(Order order) {
        Order safeOrder = Objects.requireNonNull(order, "order is required");

        OrderEntity entityToSave = orderPersistenceMapper.toEntity(safeOrder);
        OrderEntity savedEntity = orderJpaRepository.save(entityToSave);

        return orderPersistenceMapper.toDomain(savedEntity);
    }


    @Override
    public Optional<Order> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }

        return orderJpaRepository.findById(id.trim())
            .map(orderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderPersistenceMapper.toDomainList(orderJpaRepository.findAllByOrderByCreatedAtDesc());
    }

    @Override
    public List<Order> findByDateBetween(LocalDateTime start, LocalDateTime end) {
        Objects.requireNonNull(start, "start is required");
        Objects.requireNonNull(end, "end is required");
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("end must be greater than or equal to start");
        }

        return orderPersistenceMapper.toDomainList(orderJpaRepository.findAllByDateBetween(start, end));
    }
}

