package com.ortiz.tpv_backend.infrastructure.persistence.mapper;

import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.OrderEntity;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.OrderLineEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderPersistenceMapper {

    OrderEntity toEntity(Order order);

    Order toDomain(OrderEntity entity);

    List<Order> toDomainList(List<OrderEntity> entities);

    @Mapping(target = "order", ignore = true)
    OrderLineEntity toEntity(OrderLine orderLine);

    OrderLine toDomain(OrderLineEntity entity);

    @AfterMapping
    default void linkOrder(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getLines() == null) {
            return;
        }

        for (OrderLineEntity line : orderEntity.getLines()) {
            line.setOrder(orderEntity);
        }
    }
}

