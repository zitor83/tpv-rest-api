package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.application.usecase.CreateProductUseCase;
import com.ortiz.tpv_backend.application.usecase.ListProductsUseCase;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints para gestionar productos")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos obtenidos correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<List<ProductResponseDTO>>> listProducts() {
        List<ProductResponseDTO> products = listProductsUseCase.list();
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<List<ProductResponseDTO>> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Productos obtenidos correctamente", products);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear producto", description = "Crea un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Peticion invalida", content = @Content)
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<ProductResponseDTO>> createProduct(
        @Valid @RequestBody ProductRequestDTO requestDTO
    ) {
        ProductResponseDTO createdProduct = createProductUseCase.create(requestDTO);
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<ProductResponseDTO> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Producto creado correctamente", createdProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}

