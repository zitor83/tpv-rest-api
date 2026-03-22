package com.ortiz.tpv_backend.infrastructure.persistence.repository;

import com.ortiz.tpv_backend.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {

    @Override
    @EntityGraph(attributePaths = "lines")
    Optional<OrderEntity> findById(String id);

    @EntityGraph(attributePaths = "lines")
    List<OrderEntity> findAllByOrderByCreatedAtDesc();
}

