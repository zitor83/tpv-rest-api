package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.exception.ResourceNotFoundException;
import com.ortiz.tpv_backend.application.mapper.OrderMapper;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetOrderUseCaseImplTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private GetOrderUseCaseImpl getOrderUseCase;

    @Test
    void givenExistingOrderId_whenGetById_thenReturnMappedOrderResponse() {
        String orderId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        Order order = new Order(
            orderId,
            LocalDateTime.parse("2026-03-22T12:00:00"),
            List.of(new OrderLine(1L, productId, "Cafe", new BigDecimal("1.50"), 2, "Sin azucar", Map.of())),
            false,
            "Mesa 5",
            "Ana",
            "Urgente",
            "cash",
            "active",
            BigDecimal.ZERO
        );

        OrderResponseDTO responseDTO = OrderResponseDTO.builder()
            .id(orderId)
            .totalAmount(new BigDecimal("3.00"))
            .grandTotal(new BigDecimal("3.00"))
            .isForTakeaway(false)
            .tableNumber("Mesa 5")
            .customerName("Ana")
            .customNotes("Urgente")
            .paymentMethod("cash")
            .status("active")
            .tip(BigDecimal.ZERO)
            .build();

        when(orderRepositoryPort.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toResponse(order)).thenReturn(responseDTO);

        OrderResponseDTO result = getOrderUseCase.getById(orderId);

        assertEquals(orderId, result.getId());
        assertEquals(new BigDecimal("3.00"), result.getGrandTotal());
        assertEquals(false, result.getIsForTakeaway());
        assertEquals("Mesa 5", result.getTableNumber());
        assertEquals("cash", result.getPaymentMethod());
        assertEquals(BigDecimal.ZERO, result.getTip());
    }

    @Test
    void givenBlankId_whenGetById_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> getOrderUseCase.getById("  "));
    }

    @Test
    void givenMissingOrder_whenGetById_thenThrowResourceNotFoundException() {
        when(orderRepositoryPort.findById("ord-404")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> getOrderUseCase.getById("ord-404"));
    }
}

