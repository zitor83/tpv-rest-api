package com.ortiz.tpv_backend.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDomainModelTest {

    @Test
    void givenTip_whenGetGrandTotal_thenAddTipToOrderTotal() {
        Order order = new Order(
            "ord-1",
            LocalDateTime.parse("2026-03-22T10:00:00"),
            List.of(new OrderLine(1L, "prod-1", "Cafe", new BigDecimal("1.50"), 2, null, null)),
            true,
            null,
            "Cliente",
            null,
            "cash",
            "active",
            new BigDecimal("1.00")
        );

        assertEquals(new BigDecimal("3.00"), order.getTotalAmount());
        assertEquals(new BigDecimal("4.00"), order.getGrandTotal());
    }

    @Test
    void givenTakeawayAndTableNumber_whenCreate_thenThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Order(
            "ord-2",
            LocalDateTime.parse("2026-03-22T10:00:00"),
            List.of(new OrderLine(
                1L,
                "prod-1",
                "Cafe",
                new BigDecimal("1.50"),
                1,
                "sin azucar",
                Map.of("included", List.of("Cafe"), "additional", List.of("Leche"))
            )),
            true,
            "T1",
            null,
            null,
            null,
            null,
            BigDecimal.ZERO
        ));
    }
}

