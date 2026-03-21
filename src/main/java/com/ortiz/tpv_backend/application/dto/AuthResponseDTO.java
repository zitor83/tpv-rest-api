package com.ortiz.tpv_backend.application.dto;

public record AuthResponseDTO(
    String token,
    String username,
    String role
) {
}

