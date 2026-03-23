package com.ortiz.tpv_backend.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiz.tpv_backend.infrastructure.rest.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomAuthenticationEntryPoint intercepta los errores de autenticación en la capa de filtros
 * (ej. token faltante, expirado o inválido) y devuelve una respuesta JSON consistente
 * con la estructura ApiResponse en lugar de la respuesta por defecto de Spring Security.
 *
 * Se utiliza cuando:
 * - No se proporciona token en el header Authorization
 * - El token está expirado
 * - El token es inválido (formato incorrecto, firma no válida, etc.)
 *
 * Status HTTP: 401 Unauthorized
 */
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException
    ) throws IOException, ServletException {

        // Construimos la respuesta de error usando ApiResponse.error()
        ApiResponse<Void> apiResponse = ApiResponse.error("No autorizado. Token faltante o inválido.", null);

        // Configuramos los headers de la respuesta
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Escribimos el JSON directamente en el response body
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

