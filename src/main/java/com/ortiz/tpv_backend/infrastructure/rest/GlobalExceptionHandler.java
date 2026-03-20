package com.ortiz.tpv_backend.infrastructure.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}

