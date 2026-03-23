package com.ortiz.tpv_backend.domain.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public final class User {

    private final Long id;
    private final String username;
    private final String password;
    private final String role;

    public User(Long id, String username, String password, String role) {
        this.id = validateId(id);
        this.username = validateUsername(username);
        this.password = validatePassword(password);
        this.role = role == null || role.trim().isEmpty() ? "ROLE_USER" : role.trim();
    }


    private static Long validateId(Long id) {
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("id must be greater than 0");
        }
        return id;
    }

    private static String validateUsername(String username) {
        Objects.requireNonNull(username, "username is required");
        String normalized = username.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("username cannot be blank");
        }
        return normalized;
    }

    private static String validatePassword(String password) {
        Objects.requireNonNull(password, "password is required");
        if (password.isBlank()) {
            throw new IllegalArgumentException("password cannot be blank");
        }
        return password;
    }
}

