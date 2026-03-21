package com.ortiz.tpv_backend.application.dto;

public record UserResponseDTO(
    Long id,
    String username,
    String role
) {
}

