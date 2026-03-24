package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.dto.LoginResponseDTO;
import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.domain.port.PasswordEncoderPort;
import com.ortiz.tpv_backend.domain.port.TokenProviderPort;
import com.ortiz.tpv_backend.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginUseCaseImpl implements LoginUseCase {

    private static final long TOKEN_EXPIRATION_SECONDS = 3600L;

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoderPort passwordEncoderPort;
    private final TokenProviderPort tokenProviderPort;

    @Override
    public LoginResponseDTO execute(LoginRequestDTO requestDTO) {
        LoginRequestDTO safeRequest = Objects.requireNonNull(requestDTO, "requestDTO is required");

        User user = userRepositoryPort.findByUsername(safeRequest.username())
            .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!passwordEncoderPort.matches(safeRequest.password(), user.getPassword())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        String token = tokenProviderPort.generateToken(user);

        return LoginResponseDTO.builder()
            .token(token)
            .expiresIn(TOKEN_EXPIRATION_SECONDS)
            .user(LoginResponseDTO.UserData.builder()
                .id(user.getId() == null ? null : user.getId().toString())
                .username(user.getUsername())
                .role(user.getRole() == null ? null : user.getRole().toString())
                .build())
            .build();
    }
}
