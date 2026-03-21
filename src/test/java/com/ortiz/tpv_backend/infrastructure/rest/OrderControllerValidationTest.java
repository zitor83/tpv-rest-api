package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.usecase.CreateOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.GetOrderUseCase;
import com.ortiz.tpv_backend.application.usecase.ListOrdersUseCase;
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
}

