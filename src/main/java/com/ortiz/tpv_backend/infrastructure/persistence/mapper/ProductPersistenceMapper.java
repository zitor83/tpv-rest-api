package com.ortiz.tpv_backend.infrastructure.persistence.mapper;

import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductPersistenceMapper {

    ProductEntity toEntity(Product product);

    Product toDomain(ProductEntity entity);

    List<Product> toDomainList(List<ProductEntity> entities);
}

