package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.OrderRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.usecase.CreateOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.GetOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.ListOrdersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints para gestionar tickets de venta")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;

    @PostMapping
    @Operation(summary = "Crear ticket", description = "Crea un ticket de venta con calculo de precios en backend")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Ticket creado correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Peticion invalida", content = @Content),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content)
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<OrderResponseDTO>> createOrder(
        @Valid @RequestBody OrderRequestDTO requestDTO
    ) {
        OrderResponseDTO createdOrder = createOrderUseCase.create(requestDTO);
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<OrderResponseDTO> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Ticket creado correctamente", createdOrder);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener ticket", description = "Obtiene un ticket por su identificador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ticket obtenido correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "404", description = "Ticket no encontrado", content = @Content)
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<OrderResponseDTO>> getOrderById(
        @PathVariable Long id
    ) {
        OrderResponseDTO order = getOrderUseCase.getById(id);
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<OrderResponseDTO> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Ticket obtenido correctamente", order);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar tickets", description = "Obtiene todos los tickets de venta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tickets obtenidos correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<List<OrderResponseDTO>>> listOrders() {
        List<OrderResponseDTO> orders = listOrdersUseCase.list();
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<List<OrderResponseDTO>> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Tickets obtenidos correctamente", orders);

        return ResponseEntity.ok(response);
    }
}

