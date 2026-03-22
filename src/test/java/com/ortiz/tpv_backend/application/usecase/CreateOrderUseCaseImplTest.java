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
import java.util.Map;
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
            .isForTakeaway(false)
            .tableNumber("T1")
            .customerName("Juan")
            .customNotes("Sin prisa")
            .paymentMethod("card")
            .status("active")
            .tip(new BigDecimal("2.00"))
            .lines(List.of(
                OrderLineRequestDTO.builder()
                    .productId("prod-1")
                    .quantity(2)
                    .notes("Muy hecha")
                    .selectedIngredients(Map.of("included", List.of("Pan"), "additional", List.of("Bacon")))
                    .build(),
                OrderLineRequestDTO.builder()
                    .productId("prod-3")
                    .quantity(1)
                    .notes("Sin sal")
                    .selectedIngredients(Map.of("included", List.of("Patatas"), "additional", List.of()))
                    .build()
            ))
            .build();

        Product productOne = new Product(
            "prod-1",
            "Hamburguesa Clasica",
            new BigDecimal("8.50"),
            "Principal",
            "Descripcion mock",
            "http://img.com/mock.png",
            true,
            List.of("Pan", "Carne")
        );
        Product productThree = new Product(
            "prod-3",
            "Patatas Fritas",
            new BigDecimal("3.50"),
            "Entrante",
            "Descripcion mock",
            "http://img.com/mock.png",
            true,
            List.of("Patatas")
        );

        Order savedOrder = new Order(
            "ord-7",
            LocalDateTime.parse("2026-03-21T15:30:00"),
            List.of(
                new OrderLine(
                    11L,
                    "prod-1",
                    "Hamburguesa Clasica",
                    new BigDecimal("8.50"),
                    2,
                    "Muy hecha",
                    Map.of("included", List.of("Pan"), "additional", List.of("Bacon"))
                ),
                new OrderLine(
                    12L,
                    "prod-3",
                    "Patatas Fritas",
                    new BigDecimal("3.50"),
                    1,
                    "Sin sal",
                    Map.of("included", List.of("Patatas"), "additional", List.of())
                )
            ),
            false,
            "T1",
            "Juan",
            "Sin prisa",
            "card",
            "active",
            new BigDecimal("2.00")
        );

        OrderResponseDTO expected = OrderResponseDTO.builder()
            .id("ord-7")
            .createdAt(LocalDateTime.parse("2026-03-21T15:30:00"))
            .totalAmount(new BigDecimal("20.50"))
            .grandTotal(new BigDecimal("22.50"))
            .isForTakeaway(false)
            .tableNumber("T1")
            .customerName("Juan")
            .customNotes("Sin prisa")
            .paymentMethod("card")
            .status("active")
            .tip(new BigDecimal("2.00"))
            .lines(List.of(
                OrderLineResponseDTO.builder()
                    .productId("prod-1")
                    .productName("Hamburguesa Clasica")
                    .quantity(2)
                    .unitPrice(new BigDecimal("8.50"))
                    .subTotal(new BigDecimal("17.00"))
                    .notes("Muy hecha")
                    .selectedIngredients(Map.of("included", List.of("Pan"), "additional", List.of("Bacon")))
                    .build(),
                OrderLineResponseDTO.builder()
                    .productId("prod-3")
                    .productName("Patatas Fritas")
                    .quantity(1)
                    .unitPrice(new BigDecimal("3.50"))
                    .subTotal(new BigDecimal("3.50"))
                    .notes("Sin sal")
                    .selectedIngredients(Map.of("included", List.of("Patatas"), "additional", List.of()))
                    .build()
            ))
            .build();

        when(productRepositoryPort.findById("prod-1")).thenReturn(Optional.of(productOne));
        when(productRepositoryPort.findById("prod-3")).thenReturn(Optional.of(productThree));
        when(orderRepositoryPort.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponse(savedOrder)).thenReturn(expected);

        OrderResponseDTO result = createOrderUseCase.create(request);

        assertEquals(expected.getId(), result.getId());
        assertEquals(expected.getTotalAmount(), result.getTotalAmount());
        assertEquals(expected.getGrandTotal(), result.getGrandTotal());
        assertEquals(expected.getTip(), result.getTip());
        assertEquals(expected.getLines().size(), result.getLines().size());

        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(orderRepositoryPort).save(captor.capture());

        Order orderToPersist = captor.getValue();
        assertNotNull(orderToPersist.getCreatedAt());
        assertEquals(2, orderToPersist.getLines().size());
        assertEquals(new BigDecimal("20.50"), orderToPersist.getTotalAmount());
        assertEquals(new BigDecimal("22.50"), orderToPersist.getGrandTotal());
        assertEquals("active", orderToPersist.getStatus());
        assertEquals(new BigDecimal("2.00"), orderToPersist.getTip());
        assertEquals("Hamburguesa Clasica", orderToPersist.getLines().getFirst().getProductName());
        assertEquals("prod-1", orderToPersist.getLines().getFirst().getProductId());
        assertEquals("Muy hecha", orderToPersist.getLines().getFirst().getNotes());
        assertEquals(new BigDecimal("8.50"), orderToPersist.getLines().getFirst().getUnitPrice());
    }

    @Test
    void givenMissingProduct_whenCreate_thenThrowNotFoundAndDoNotPersist() {
        OrderRequestDTO request = OrderRequestDTO.builder()
            .isForTakeaway(true)
            .lines(List.of(OrderLineRequestDTO.builder().productId("prod-999").quantity(1).build()))
            .build();

        when(productRepositoryPort.findById("prod-999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> createOrderUseCase.create(request));

        verify(orderRepositoryPort, never()).save(any(Order.class));
    }
}

