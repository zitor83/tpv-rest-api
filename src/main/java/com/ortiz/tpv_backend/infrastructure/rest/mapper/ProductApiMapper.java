package com.ortiz.tpv_backend.infrastructure.rest.mapper;

import com.ortiz.tpv_backend.application.dto.ProductRequestDTO;
import com.ortiz.tpv_backend.application.dto.ProductResponseDTO;
import com.ortiz.tpv_backend.domain.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductApiMapper {

    @Mapping(target = "id", expression = "java(null)")
    Product toDomain(ProductRequestDTO requestDTO);

    ProductResponseDTO toResponse(Product product);
}

