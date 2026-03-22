package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.usecase.CreateProductUseCase;
import com.ortiz.tpv_backend.application.usecase.ListProductsUseCase;
import com.ortiz.tpv_backend.domain.port.TokenProviderPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class ProductControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateProductUseCase createProductUseCase;

    @MockitoBean
    private ListProductsUseCase listProductsUseCase;

    @MockitoBean
    private TokenProviderPort tokenProviderPort;

    @Test
    void givenInvalidRequest_whenCreateProduct_thenReturnApiResponseValidationError() throws Exception {
        String invalidRequest = """
            {
              "id": "prod-123",
              "price": 10.5,
              "category": "Bebidas",
              "description": "Descripcion mock",
              "imageUrl": "http://img.com/mock.png",
              "available": true,
              "defaultIngredients": ["Pan", "Carne"]
            }
            """;

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Error de validacion"))
            .andExpect(jsonPath("$.data.name").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }
}


