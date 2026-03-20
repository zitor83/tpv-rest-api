package com.ortiz.tpv_backend.infrastructure.persistence;

import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.domain.port.ProductRepositoryPort;
import com.ortiz.tpv_backend.infrastructure.persistence.entity.ProductEntity;
import com.ortiz.tpv_backend.infrastructure.persistence.mapper.ProductPersistenceMapper;
import com.ortiz.tpv_backend.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository productJpaRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    @Override
    public Product save(Product product) {
        Product safeProduct = Objects.requireNonNull(product, "product is required");

        ProductEntity entityToSave = productPersistenceMapper.toEntity(safeProduct);
        ProductEntity savedEntity = productJpaRepository.save(entityToSave);

        return productPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
            .map(productPersistenceMapper::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return productPersistenceMapper.toDomainList(productJpaRepository.findAll());
    }
}

