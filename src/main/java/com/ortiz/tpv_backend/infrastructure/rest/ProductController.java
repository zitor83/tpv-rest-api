package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.application.usecase.CreateProductUseCase;
import com.ortiz.tpv_backend.application.usecase.ListProductsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints para gestionar el catálogo de productos del TPV")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    @GetMapping
    @Operation(
        summary = "Listar todos los productos",
        description = "Obtiene el listado completo de productos disponibles del catálogo, incluyendo nombre, precio, descripción e ingredientes por defecto.",
        tags = {"Products"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Productos obtenidos correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> listProducts() {
        List<ProductResponseDTO> products = listProductsUseCase.list();
        ApiResponse<List<ProductResponseDTO>> response = ApiResponse.success("Productos obtenidos correctamente", products);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
        summary = "Crear nuevo producto",
        description = "Crea un nuevo producto en el catálogo con nombre, precio, descripción e ingredientes por defecto.",
        tags = {"Products"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Producto creado correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validación fallida: campos requeridos inválidos o ausentes",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(
        @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        ProductResponseDTO createdProduct = createProductUseCase.create(requestDTO);
        ApiResponse<ProductResponseDTO> response = ApiResponse.success("Producto creado correctamente", createdProduct);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}



