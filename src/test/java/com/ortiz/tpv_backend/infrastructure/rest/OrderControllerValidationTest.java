package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.usecase.CreateOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.GetOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.ListOrdersUseCase;
import com.ortiz.tpv_backend.domain.port.TokenProviderPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class OrderControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateOrderUseCase createOrderUseCase;

    @MockitoBean
    private GetOrderUseCase getOrderUseCase;

    @MockitoBean
    private ListOrdersUseCase listOrdersUseCase;

    @MockitoBean
    private TokenProviderPort tokenProviderPort;

    @Test
    void givenInvalidRequest_whenCreateOrder_thenReturnApiResponseValidationError() throws Exception {
        String invalidRequest = """
            {
              "lines": []
            }
            """;

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Error de validacion"))
            .andExpect(jsonPath("$.data.lines").exists())
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void givenIllegalArgument_whenGetOrder_thenReturnBadRequestApiResponse() throws Exception {
        when(getOrderUseCase.getById(0L)).thenThrow(new IllegalArgumentException("id must be greater than 0"));

        mockMvc.perform(get("/api/orders/0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("id must be greater than 0"))
            .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void givenUnexpectedError_whenCreateOrder_thenReturnInternalServerErrorApiResponse() throws Exception {
        String validRequest = """
            {
              "isForTakeaway": false,
              "tableNumber": "Mesa 5",
              "status": "active",
              "paymentMethod": "cash",
              "tip": 0,
              "lines": [
                {
                  "productId": "prod-1",
                  "quantity": 1,
                  "notes": "sin sal",
                  "selectedIngredients": {
                    "included": ["Patatas"],
                    "additional": []
                  }
                }
              ]
            }
            """;

        when(createOrderUseCase.create(org.mockito.ArgumentMatchers.any()))
            .thenThrow(new RuntimeException("Fallo inesperado de prueba"));

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequest))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.success").value(false))
            .andExpect(jsonPath("$.message").value("Error interno del servidor"))
            .andExpect(jsonPath("$.data").value("Fallo inesperado de prueba"))
            .andExpect(jsonPath("$.timestamp").exists());
    }
}

