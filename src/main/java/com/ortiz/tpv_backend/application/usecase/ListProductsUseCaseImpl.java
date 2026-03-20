package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.application.mapper.ProductMapper;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListProductsUseCaseImpl implements ListProductsUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductMapper productMapper;

    @Override
    public List<ProductResponseDTO> list() {
        return productRepositoryPort.findAll().stream()
            .map(productMapper::toResponse)
            .toList();
    }
}

