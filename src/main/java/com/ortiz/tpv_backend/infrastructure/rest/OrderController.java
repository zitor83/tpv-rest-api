package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.OrderRequestDTO;
import com.ortiz.tpv_backend.application.dto.OrderResponseDTO;
import com.ortiz.tpv_backend.application.dto.OrderUpdateRequestDTO;
import com.ortiz.tpv_backend.application.usecase.CreateOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.GetOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.ListOrdersUseCase;
import com.ortiz.tpv_backend.application.usecase.UpdateOrderUseCase;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Gestiona tickets de venta, incluyendo creacion, consulta por id y listado con filtros operativos.")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final ListOrdersUseCase listOrdersUseCase;
    private final UpdateOrderUseCase updateOrderUseCase;

    @PostMapping
    @Operation(
        summary = "Crear nuevo ticket de venta",
        description = "Registra un ticket de venta a partir de lineas con productId y cantidad. El backend recalcula importes con precios vigentes, persiste la orden y devuelve el ticket completo.",
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
        description = "Devuelve el detalle completo de un ticket por su identificador, incluyendo lineas, estado y total calculado para auditoria o reimpresion.",
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

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar ticket de venta",
        description = "Actualiza de forma parcial un ticket existente. Permite reemplazar items o modificar estado, pago, propina y datos operativos del pedido.",
        tags = {"Orders"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Ticket actualizado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validación fallida o datos incompatibles del pedido",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Ticket o producto no encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "No autenticado",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<OrderResponseDTO>> updateOrder(
        @PathVariable String id,
        @Valid @RequestBody OrderUpdateRequestDTO requestDTO
    ) {
        OrderResponseDTO updatedOrder = updateOrderUseCase.update(id, requestDTO);
        ApiResponse<OrderResponseDTO> response = ApiResponse.success("Ticket actualizado correctamente", updatedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
        summary = "Listar todos los tickets",
        description = "Obtiene tickets de venta y admite filtros por date y status, junto con paginacion por page y limit. Cuando se informa date, incluye resumen agregado de ventas en la respuesta.",
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
    public ResponseEntity<ApiResponse<List<OrderResponseDTO>>> listOrders(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) LocalDate date
    ) {
        List<OrderResponseDTO> orders = listOrdersUseCase.list(date, status);

        if (date != null) {
            BigDecimal total = orders.stream()
                .map(order -> order.getTotalAmount() == null ? BigDecimal.ZERO : order.getTotalAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            Map<String, Object> summary = Map.of(
                "count", orders.size(),
                "totalOrders", orders.size(),
                "totalSales", total
            );
            ApiResponse<List<OrderResponseDTO>> response = ApiResponse.success(
                "Tickets obtenidos correctamente",
                orders,
                Map.of("summary", summary)
            );
            return ResponseEntity.ok(response);
        }

        int safePage = page == null || page < 1 ? 1 : page;
        int safeLimit = limit == null || limit < 1 ? 50 : limit;
        int fromIndex = Math.min((safePage - 1) * safeLimit, orders.size());
        int toIndex = Math.min(fromIndex + safeLimit, orders.size());
        List<OrderResponseDTO> pagedOrders = orders.subList(fromIndex, toIndex);

        Map<String, Object> pagination = Map.of(
            "page", safePage,
            "limit", safeLimit,
            "total", orders.size()
        );

        ApiResponse<List<OrderResponseDTO>> response = ApiResponse.success(
            "Tickets obtenidos correctamente",
            pagedOrders,
            Map.of("pagination", pagination)
        );
        return ResponseEntity.ok(response);
    }
}

