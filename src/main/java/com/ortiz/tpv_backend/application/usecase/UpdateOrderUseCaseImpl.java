package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderLineRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.dto.OrderUpdateRequestDTO;
import com.ortiz.tpv_backend.application.dto.SelectedIngredientsDTO;
import com.ortiz.tpv_backend.application.exception.ResourceNotFoundException;
import com.ortiz.tpv_backend.application.mapper.OrderMapper;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO update(String id, OrderUpdateRequestDTO requestDTO) {
        String safeId = normalizeId(id);
        OrderUpdateRequestDTO safeRequest = Objects.requireNonNull(requestDTO, "requestDTO is required");

        Order currentOrder = orderRepositoryPort.findById(safeId)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket no encontrado con id: " + safeId));

        List<OrderLine> mergedLines = safeRequest.getItems() == null
            ? currentOrder.getLines()
            : safeRequest.getItems().stream().map(this::buildOrderLine).toList();

        boolean mergedIsForTakeaway = safeRequest.getIsForTakeaway() != null
            ? safeRequest.getIsForTakeaway()
            : currentOrder.isForTakeaway();

        Order orderToUpdate = new Order(
            currentOrder.getId(),
            currentOrder.getCreatedAt(),
            mergedLines,
            mergedIsForTakeaway,
            pick(safeRequest.getTableNumber(), currentOrder.getTableNumber()),
            pick(safeRequest.getCustomerName(), currentOrder.getCustomerName()),
            pick(safeRequest.getCustomNotes(), currentOrder.getCustomNotes()),
            pick(safeRequest.getPaymentMethod(), currentOrder.getPaymentMethod()),
            pick(safeRequest.getStatus(), currentOrder.getStatus()),
            pick(safeRequest.getTip(), currentOrder.getTip())
        );

        Order updatedOrder = orderRepositoryPort.save(orderToUpdate);
        return orderMapper.toResponse(updatedOrder);
    }

    private OrderLine buildOrderLine(OrderLineRequestDTO lineRequestDTO) {
        OrderLineRequestDTO safeLine = Objects.requireNonNull(lineRequestDTO, "order line is required");
        String productId = Objects.requireNonNull(safeLine.getProductId(), "productId is required");
        Integer quantity = Objects.requireNonNull(safeLine.getQuantity(), "quantity is required");

        Product product = productRepositoryPort.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));

        return new OrderLine(
            null,
            product.getId(),
            product.getName(),
            product.getPrice(),
            quantity,
            safeLine.getNotes(),
            selectedIngredientsToMap(safeLine.getSelectedIngredients())
        );
    }

    private static String normalizeId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("id is required");
        }
        return id.trim();
    }

    private static java.util.Map<String, java.util.List<String>> selectedIngredientsToMap(SelectedIngredientsDTO selectedIngredients) {
        if (selectedIngredients == null) {
            return java.util.Map.of();
        }
        return selectedIngredients.toMap();
    }

    private static <T> T pick(T incoming, T current) {
        return incoming != null ? incoming : current;
    }

    private static BigDecimal pick(BigDecimal incoming, BigDecimal current) {
        return incoming != null ? incoming : current;
    }
}


