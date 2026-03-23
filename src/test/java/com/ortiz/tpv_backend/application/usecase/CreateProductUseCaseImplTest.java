package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ProductMapper;
import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProductUseCaseImplTest {

    @Mock
    private ProductRepositoryPort productRepositoryPort;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private CreateProductUseCaseImpl createProductUseCase;

    @Test
    void givenValidRequest_whenCreate_thenMapsSavesAndReturnsMappedResponse() {
        ProductRequestDTO request = ProductRequestDTO.builder()
            .id("prod-123")
            .name("Cafe")
            .price(new BigDecimal("1.50"))
            .category("Bebidas")
            .description("Descripcion mock")
            .imageUrl("http://img.com/mock.png")
            .available(true)
            .defaultIngredients(List.of("Cafe", "Agua"))
            .build();

        Product mappedProduct = new Product(
            "prod-123",
            "Cafe",
            new BigDecimal("1.50"),
            "Bebidas",
            "Descripcion mock",
            "http://img.com/mock.png",
            true,
            List.of("Cafe", "Agua")
        );
        Product savedProduct = new Product(
            "prod-123",
            "Cafe",
            new BigDecimal("1.50"),
            "Bebidas",
            "Descripcion mock",
            "http://img.com/mock.png",
            true,
            List.of("Cafe", "Agua")
        );
        ProductResponseDTO expectedResponse = ProductResponseDTO.builder()
            .id("prod-123")
            .name("Cafe")
            .price(new BigDecimal("1.50"))
            .category("Bebidas")
            .description("Descripcion mock")
            .imageUrl("http://img.com/mock.png")
            .available(true)
            .defaultIngredients(List.of("Cafe", "Agua"))
            .build();

        when(productMapper.toDomain(request)).thenReturn(mappedProduct);
        when(productRepositoryPort.save(mappedProduct)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(expectedResponse);

        ProductResponseDTO result = createProductUseCase.create(request);

        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getPrice(), result.getPrice());
        assertEquals(expectedResponse.getCategory(), result.getCategory());
        assertEquals(expectedResponse.getDescription(), result.getDescription());
        assertEquals(expectedResponse.getImageUrl(), result.getImageUrl());
        assertEquals(expectedResponse.getAvailable(), result.getAvailable());
        assertEquals(expectedResponse.getDefaultIngredients(), result.getDefaultIngredients());

        InOrder inOrder = inOrder(productMapper, productRepositoryPort, productMapper);
        inOrder.verify(productMapper).toDomain(request);
        inOrder.verify(productRepositoryPort).save(mappedProduct);
        inOrder.verify(productMapper).toResponse(savedProduct);
    }

    @Test
    void givenNullRequest_whenCreate_thenThrowAndDoNotCallCollaborators() {
        assertThrows(NullPointerException.class, () -> createProductUseCase.create(null));

        verifyNoInteractions(productMapper, productRepositoryPort);
    }
}

