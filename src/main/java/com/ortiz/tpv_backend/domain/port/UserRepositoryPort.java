package com.ortiz.tpv_backend.domain.port;

import com.ortiz.tpv_backend.domain.model.User;

import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(String username);
}

