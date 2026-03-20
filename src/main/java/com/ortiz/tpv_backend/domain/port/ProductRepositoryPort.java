package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}

