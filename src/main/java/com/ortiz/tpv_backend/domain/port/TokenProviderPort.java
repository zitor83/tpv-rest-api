package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.User;

public interface TokenProviderPort {

    String generateToken(User user);

    boolean isValid(String token);

    String extractUsername(String token);
}

