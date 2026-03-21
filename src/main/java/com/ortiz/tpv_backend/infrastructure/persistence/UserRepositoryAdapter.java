package com.ortiz.tpv_backend.infrastructure.persistence;

import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.domain.port.UserRepositoryPort;
import com.ortiz.tpv_backend.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.ortiz.tpv_backend.infrastructure.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public Optional<User> findByUsername(String username) {
        String safeUsername = Objects.requireNonNull(username, "username is required").trim();
        return userJpaRepository.findByUsername(safeUsername)
            .map(userPersistenceMapper::toDomain);
    }
}

