package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.OrderLineRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderLineResponseDTO;
import com.ortiz.tpv_backend.application.dto.OrderRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.exception.ResourceNotFoundException;
import com.ortiz.tpv_backend.application.mapper.OrderMapper;
import com.ortiz.tpv_backend.domain.model.Order;
import com.ortiz.tpv_backend.domain.model.OrderLine;
import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.domain.port.OrderRepositoryPort;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseImplTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCase;

    @Test
    void givenValidRequest_whenCreate_thenBuildOrderWithDatabasePrices() {
        OrderRequestDTO request = OrderRequestDTO.builder()
            .lines(List.of(
                OrderLineRequestDTO.builder().productId(1L).quantity(2).build(),
                OrderLineRequestDTO.builder().productId(3L).quantity(1).build()
            ))
            .build();

        Product productOne = new Product(1L, "Hamburguesa Clasica", new BigDecimal("8.50"), "Principal");
        Product productThree = new Product(3L, "Patatas Fritas", new BigDecimal("3.50"), "Entrante");

        Order savedOrder = new Order(
            7L,
            LocalDateTime.parse("2026-03-21T15:30:00"),
            List.of(
                new OrderLine(11L, 1L, "Hamburguesa Clasica", new BigDecimal("8.50"), 2),
                new OrderLine(12L, 3L, "Patatas Fritas", new BigDecimal("3.50"), 1)
            )
        );

        OrderResponseDTO expected = OrderResponseDTO.builder()
            .id(7L)
            .createdAt(LocalDateTime.parse("2026-03-21T15:30:00"))
            .totalAmount(new BigDecimal("20.50"))
            .lines(List.of(
                OrderLineResponseDTO.builder()
                    .productId(1L)
                    .productName("Hamburguesa Clasica")
                    .quantity(2)
                    .unitPrice(new BigDecimal("8.50"))
                    .subTotal(new BigDecimal("17.00"))
                    .build(),
                OrderLineResponseDTO.builder()
                    .productId(3L)
                    .productName("Patatas Fritas")
                    .quantity(1)
                    .unitPrice(new BigDecimal("3.50"))
                    .subTotal(new BigDecimal("3.50"))
                    .build()
            ))
            .build();

        when(productRepositoryPort.findById(1L)).thenReturn(Optional.of(productOne));
        when(productRepositoryPort.findById(3L)).thenReturn(Optional.of(productThree));
        when(orderRepositoryPort.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(expected);

        OrderResponseDTO result = createOrderUseCase.create(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTotalAmount(), result.getTotalAmount());
        assertEquals(expected.getLines().size(), result.getLines().size());

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepositoryPort).save(captor.capture());

        Order orderToPersist = captor.getValue();
        assertNotNull(orderToPersist.getCreatedAt());
        assertEquals(2, orderToPersist.getLines().size());
        assertEquals(new BigDecimal("20.50"), orderToPersist.getTotalAmount());
        assertEquals("Hamburguesa Clasica", orderToPersist.getLines().getFirst().getProductName());
        assertEquals(new BigDecimal("8.50"), orderToPersist.getLines().getFirst().getUnitPrice());
    }

    @Test
    void givenMissingProduct_whenCreate_thenThrowNotFoundAndDoNotPersist() {
        OrderRequestDTO request = OrderRequestDTO.builder()
            .lines(List.of(OrderLineRequestDTO.builder().productId(999L).quantity(1).build()))
            .build();

        when(productRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createOrderUseCase.create(request));

        verify(orderRepositoryPort, never()).save(any(Order.class));
    }
}

