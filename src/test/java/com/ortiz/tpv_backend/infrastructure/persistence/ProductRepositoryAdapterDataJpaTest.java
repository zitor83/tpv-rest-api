package com.ortiz.tpv_backend.infrastructure.persistence;

import com.ortiz.tpv_backend.domain.model.Product;
import com.ortiz.tpv_backend.infrastructure.persistence.mapper.ProductPersistenceMapperImpl;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ContextConfiguration(classes = ProductRepositoryAdapterDataJpaTest.TestApplication.class)
@Import({ProductRepositoryAdapter.class, ProductPersistenceMapperImpl.class})
class ProductRepositoryAdapterDataJpaTest {

    @Autowired
    private ProductRepositoryAdapter productRepositoryAdapter;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @SpringBootApplication(scanBasePackageClasses = ProductRepositoryAdapter.class)
    static class TestApplication {
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM products");
    }

    @Test
    void save_shouldPersistAndMapBackDomain() {
        Product productToSave = new Product(
            null,
            "Burger",
            new BigDecimal("10.50"),
            "Food",
            "Classic burger",
            "http://img.com/burger.png",
            true,
            List.of("Bread", "Meat", "Cheese")
        );

        Product saved = productRepositoryAdapter.save(productToSave);

        assertNotNull(saved.getId());
        assertEquals("Burger", saved.getName());
        assertEquals(new BigDecimal("10.50"), saved.getPrice());
        assertEquals("Food", saved.getCategory());
        assertEquals("Classic burger", saved.getDescription());
        assertEquals("http://img.com/burger.png", saved.getImageUrl());
        assertTrue(saved.isAvailable());
        assertEquals(List.of("Bread", "Meat", "Cheese"), saved.getDefaultIngredients());
    }

    @Test
    void findById_shouldReturnMappedDomain() {
        Product saved = productRepositoryAdapter.save(new Product(
            null,
            "Tea",
            new BigDecimal("2.20"),
            "Drinks",
            "Green tea",
            "http://img.com/tea.png",
            true,
            List.of("Tea", "Water")
        ));

        entityManager.flush();
        entityManager.clear();

        var found = productRepositoryAdapter.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
        assertEquals("Tea", found.get().getName());
        assertEquals(List.of("Tea", "Water"), found.get().getDefaultIngredients());
    }

    @Test
    void findAll_shouldReturnAllMappedDomains() {
        productRepositoryAdapter.save(new Product(
            null,
            "Coffee",
            new BigDecimal("1.50"),
            "Drinks",
            "Black coffee",
            "http://img.com/coffee.png",
            true,
            List.of("Coffee", "Water")
        ));
        productRepositoryAdapter.save(new Product(
            null,
            "Fries",
            new BigDecimal("3.50"),
            "Food",
            "Crispy fries",
            "http://img.com/fries.png",
            true,
            List.of("Potato", "Salt")
        ));

        List<Product> products = productRepositoryAdapter.findAll();

        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(product -> "Coffee".equals(product.getName())));
        assertTrue(products.stream().anyMatch(product -> "Fries".equals(product.getName())));
    }

    @Test
    void save_withDefaultIngredients_shouldRoundTripJsonList() {
        Product saved = productRepositoryAdapter.save(new Product(
            null,
            "Pizza",
            new BigDecimal("12.00"),
            "Food",
            "Margherita",
            "http://img.com/pizza.png",
            true,
            List.of("Tomato", "Cheese")
        ));

        entityManager.flush();
        entityManager.clear();

        String rawJson = jdbcTemplate.queryForObject(
            "SELECT default_ingredients FROM products WHERE id = ?",
            String.class,
            saved.getId()
        );

        assertNotNull(rawJson);
        assertTrue(rawJson.contains("\"Tomato\""));
        assertTrue(rawJson.contains("\"Cheese\""));

        var reloaded = productRepositoryAdapter.findById(saved.getId());

        assertTrue(reloaded.isPresent());
        assertEquals(List.of("Tomato", "Cheese"), reloaded.get().getDefaultIngredients());
        assertFalse(reloaded.get().getDefaultIngredients().isEmpty());
    }
}




