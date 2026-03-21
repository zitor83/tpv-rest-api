package com.ortiz.tpv_backend.application.mapper;

import com.ortiz.tpv_backend.application.dto.OrderLineResponseDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDTO toResponse(Order order);

    OrderLineResponseDTO toResponse(OrderLine orderLine);

    List<OrderResponseDTO> toResponseList(List<Order> orders);
}

