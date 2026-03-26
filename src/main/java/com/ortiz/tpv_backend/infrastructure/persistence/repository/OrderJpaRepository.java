package com.ortiz.tpv_backend.infrastructure.persistence.repository;

import com.ortiz.tpv_backend.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, String> {

    @Override
    @EntityGraph(attributePaths = "lines")
    Optional<OrderEntity> findById(String id);

    @EntityGraph(attributePaths = "lines")
    List<OrderEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths = "lines")
    List<OrderEntity> findAllByStatusIgnoreCaseOrderByCreatedAtDesc(String status);

    @EntityGraph(attributePaths = "lines")
    @Query("select o from OrderEntity o where o.createdAt between :start and :end order by o.createdAt asc")
    List<OrderEntity> findAllByDateBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @EntityGraph(attributePaths = "lines")
    @Query("select o from OrderEntity o where o.createdAt between :start and :end and lower(o.status) = lower(:status) order by o.createdAt asc")
    List<OrderEntity> findAllByDateBetweenAndStatus(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("status") String status);
}

