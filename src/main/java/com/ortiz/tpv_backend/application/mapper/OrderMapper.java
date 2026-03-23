package com.ortiz.tpv_backend.application.mapper;

import com.ortiz.tpv_backend.application.dto.OrderLineResponseDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.dto.SelectedIngredientsDTO;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "grandTotal", expression = "java(order.getGrandTotal())")
    @Mapping(target = "isForTakeaway", expression = "java(order.isForTakeaway())")
    OrderResponseDTO toResponse(Order order);

    @Mapping(target = "selectedIngredients", expression = "java(SelectedIngredientsDTO.fromMap(orderLine.getSelectedIngredients()))")
    OrderLineResponseDTO toResponse(OrderLine orderLine);

    List<OrderResponseDTO> toResponseList(List<Order> orders);
}

