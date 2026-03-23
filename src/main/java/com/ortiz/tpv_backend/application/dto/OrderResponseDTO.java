package com.ortiz.tpv_backend.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonProperty("total")
    private BigDecimal totalAmount;
    private BigDecimal grandTotal;
    private String tableNumber;
    private Boolean isForTakeaway;
    private String customerName;
    private String customNotes;
    private String paymentMethod;
    private String status;
    private BigDecimal tip;
    @JsonProperty("items")
    private List<OrderLineResponseDTO> lines;
}

