package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.UserResponseDTO;
import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetMeUseCaseImpl implements GetMeUseCase {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public UserResponseDTO execute(String username) {
        String safeUsername = Objects.requireNonNull(username, "username is required").trim();
        if (safeUsername.isEmpty()) {
            throw new IllegalArgumentException("username cannot be blank");
        }

        User user = userRepositoryPort.findByUsername(safeUsername)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole());
    }
}

