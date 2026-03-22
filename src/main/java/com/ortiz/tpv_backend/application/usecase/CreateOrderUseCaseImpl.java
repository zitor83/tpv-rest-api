package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderLineRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
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

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepositoryPort orderRepositoryPort;
    private final ProductRepositoryPort productRepositoryPort;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDTO create(OrderRequestDTO requestDTO) {
        OrderRequestDTO safeRequest = Objects.requireNonNull(requestDTO, "requestDTO is required");
        List<OrderLineRequestDTO> safeLines = Objects.requireNonNull(safeRequest.getLines(), "lines are required");

        List<OrderLine> lines = safeLines.stream()
            .map(this::buildOrderLine)
            .toList();

        Boolean isForTakeaway = Objects.requireNonNull(safeRequest.getIsForTakeaway(), "isForTakeaway is required");

        Order orderToCreate = Order.create(
            lines,
            isForTakeaway,
            safeRequest.getTableNumber(),
            safeRequest.getCustomerName(),
            safeRequest.getCustomNotes(),
            safeRequest.getPaymentMethod(),
            safeRequest.getStatus(),
            safeRequest.getTip()
        );
        Order createdOrder = orderRepositoryPort.save(orderToCreate);

        return orderMapper.toResponse(createdOrder);
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
            safeLine.getSelectedIngredients()
        );
    }
}


