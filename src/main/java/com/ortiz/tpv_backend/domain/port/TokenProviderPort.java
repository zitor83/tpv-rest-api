package com.ortiz.tpv_backend.domain.port;

public interface TokenProviderPort {

    String generateToken(String subject);

    boolean isTokenValid(String token);

    String extractSubject(String token);
}

