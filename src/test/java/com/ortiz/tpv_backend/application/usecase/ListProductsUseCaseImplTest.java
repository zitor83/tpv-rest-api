package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ProductMapper;
import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListProductsUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ListProductsUseCaseImpl listProductsUseCase;

    @Test
    void givenProductsInRepository_whenList_thenReturnMappedResponsesWithExtendedFields() {
        Product product = new Product(
            "prod-123",
            "Cafe",
            new BigDecimal("1.50"),
            "Bebidas",
            "Descripcion mock",
            "http://img.com/mock.png",
            true,
            List.of("Cafe", "Agua")
        );

        ProductResponseDTO responseDTO = ProductResponseDTO.builder()
            .id("prod-123")
            .name("Cafe")
            .price(new BigDecimal("1.50"))
            .category("Bebidas")
            .description("Descripcion mock")
            .imageUrl("http://img.com/mock.png")
            .available(true)
            .defaultIngredients(List.of("Cafe", "Agua"))
            .build();

        when(productRepositoryPort.findAll()).thenReturn(List.of(product));
        when(productMapper.toResponse(product)).thenReturn(responseDTO);

        List<ProductResponseDTO> result = listProductsUseCase.list();

        assertEquals(1, result.size());
        assertEquals("prod-123", result.getFirst().getId());
        assertEquals("Descripcion mock", result.getFirst().getDescription());
        assertEquals("http://img.com/mock.png", result.getFirst().getImageUrl());
        assertEquals(true, result.getFirst().getAvailable());
        assertEquals(List.of("Cafe", "Agua"), result.getFirst().getDefaultIngredients());
    }
}

