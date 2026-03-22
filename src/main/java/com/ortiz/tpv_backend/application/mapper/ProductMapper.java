package com.ortiz.tpv_backend.application.mapper;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.domain.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toDomain(ProductRequestDTO requestDTO);

    ProductResponseDTO toResponse(Product product);
}

