package com.ortiz.tpv_backend.infrastructure.security;

import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.domain.port.TokenProviderPort;
import com.ortiz.tpv_backend.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtAdapter implements TokenProviderPort {

    private final JwtProperties jwtProperties;

    public JwtAdapter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
            .subject(user.getUsername())
            .claim("role", user.getRole())
            .issuer(jwtProperties.issuer())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtProperties.expirationMillis()))
            .signWith(getSigningKey())
            .compact();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public String extractUsername(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }
}

