package com.ortiz.tpv_backend.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private String id;
    private LocalDateTime createdAt;
    private BigDecimal totalAmount;
    private BigDecimal grandTotal;
    private String tableNumber;
    private Boolean isForTakeaway;
    private String customerName;
    private String customNotes;
    private String paymentMethod;
    private String status;
    private BigDecimal tip;
    private List<OrderLineResponseDTO> lines;
}

