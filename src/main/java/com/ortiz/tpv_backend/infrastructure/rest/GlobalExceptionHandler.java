package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.exception.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new LinkedHashMap<>();

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            String message = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("Valor invalido");
            errors.putIfAbsent(fieldError.getField(), message);
        }

        ApiResponse<Map<String, String>> response = ApiResponse.error("Error de validacion", errors);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage(), null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException exception) {
        ApiResponse<Void> response = ApiResponse.error(exception.getMessage(), null);
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        ApiResponse<Void> response = ApiResponse.error(
            "No se puede realizar la accion porque el registro esta en uso o duplicado",
            null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneralException(Exception exception) {
        ApiResponse<String> response = ApiResponse.error("Error interno del servidor", exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        // Captura JSONs mal formados o tipos de datos incorrectos enviados en el body
        ApiResponse<Void> response = ApiResponse.error("Formato JSON inválido o tipo de dato incorrecto en la petición", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatch(MethodArgumentTypeMismatchException exception) {
        // Captura errores cuando se envía un texto en la URL donde se esperaba un número/UUID
        ApiResponse<Void> response = ApiResponse.error("Tipo de parámetro inválido en la URL", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException exception) {
        // Captura intentos de acceder a rutas protegidas sin el rol adecuado
        ApiResponse<Void> response = ApiResponse.error("No tienes permisos para realizar esta acción", null);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(AuthenticationException exception) {
        // Captura intentos de acceso sin token o con token expirado a nivel de controlador
        ApiResponse<Void> response = ApiResponse.error("Autenticación requerida o credenciales inválidas", null);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

