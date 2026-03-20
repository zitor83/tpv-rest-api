package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ProductMapper;
import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
            .name("Cafe")
            .price(new BigDecimal("1.50"))
            .category("Bebidas")
            .build();

        Product mappedProduct = new Product(null, "Cafe", new BigDecimal("1.50"), "Bebidas");
        Product savedProduct = new Product(10L, "Cafe", new BigDecimal("1.50"), "Bebidas");
        ProductResponseDTO expectedResponse = ProductResponseDTO.builder()
            .id(10L)
            .name("Cafe")
            .price(new BigDecimal("1.50"))
            .category("Bebidas")
            .build();

        when(productMapper.toDomain(request)).thenReturn(mappedProduct);
        when(productRepositoryPort.save(mappedProduct)).thenReturn(savedProduct);
        when(productMapper.toResponse(savedProduct)).thenReturn(expectedResponse);

        ProductResponseDTO result = createProductUseCase.create(request);

        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getPrice(), result.getPrice());
        assertEquals(expectedResponse.getCategory(), result.getCategory());

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

