package com.ortiz.tpv_backend.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ortiz.tpv_backend.infrastructure.rest.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * CustomAccessDeniedHandler intercepta los errores de autorización cuando un usuario
 * autenticado (con token válido) intenta acceder a un recurso para el cual no tiene
 * permisos suficientes.
 *
 * Se utiliza cuando:
 * - Usuario es ROLE_USER pero intenta acceder a @PreAuthorize("hasRole('ADMIN')")
 * - Usuario no tiene un permiso requerido por @PreAuthorize
 * - El token es válido pero los permisos asociados son insuficientes
 *
 * Diferencia con AuthenticationEntryPoint:
 * - AuthenticationEntryPoint (401): Problema de AUTENTICACIÓN (no hay usuario)
 * - AccessDeniedHandler (403): Problema de AUTORIZACIÓN (hay usuario, pero sin permisos)
 *
 * Status HTTP: 403 Forbidden
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(
        HttpServletRequest request,
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        // Construimos la respuesta de error usando ApiResponse.error()
        ApiResponse<Void> apiResponse = ApiResponse.error(
            "Acceso denegado. No tienes permisos para acceder a este recurso.",
            null
        );

        // Configuramos los headers de la respuesta
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Escribimos el JSON directamente en el response body
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

