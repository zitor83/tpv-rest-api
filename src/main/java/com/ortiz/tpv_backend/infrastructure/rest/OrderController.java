package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.OrderRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.usecase.CreateOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.GetOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.ListOrdersUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints para gestionar tickets/órdenes de venta. Registra productos, cantidades y calcula totales en el backend.")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;

    @PostMapping
    @Operation(
        summary = "Crear nuevo ticket de venta",
        description = "Crea una nueva orden de venta. El frontend envía solo IDs de productos y cantidades; el backend busca los precios reales en BD y calcula el total.",
        tags = {"Orders"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Ticket creado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validación fallida: líneas vacías o cantidades inválidas",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto referenciado en el ticket no existe en catálogo",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<OrderResponseDTO>> createOrder(
        @Valid @RequestBody OrderRequestDTO requestDTO
    ) {
        OrderResponseDTO createdOrder = createOrderUseCase.create(requestDTO);
        ApiResponse<OrderResponseDTO> response = ApiResponse.success("Ticket creado correctamente", createdOrder);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener detalle de un ticket",
        description = "Recupera el detalle completo de un ticket (órdenes + líneas de venta) por su identificador único.",
        tags = {"Orders"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Ticket obtenido correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Ticket con el ID especificado no existe",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<OrderResponseDTO>> getOrderById(
        @PathVariable String id
    ) {
        OrderResponseDTO order = getOrderUseCase.getById(id);
        ApiResponse<OrderResponseDTO> response = ApiResponse.success("Ticket obtenido correctamente", order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar todos los tickets",
        description = "Obtiene el listado completo de tickets de venta. Útil para cierre de caja y reportes de ventas diarias.",
        tags = {"Orders"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Tickets obtenidos correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> listOrders() {
        List<OrderResponseDTO> orders = listOrdersUseCase.list();
        ApiResponse<List<OrderResponseDTO>> response = ApiResponse.success("Tickets obtenidos correctamente", orders);
        return ResponseEntity.ok(response);
    }
}

