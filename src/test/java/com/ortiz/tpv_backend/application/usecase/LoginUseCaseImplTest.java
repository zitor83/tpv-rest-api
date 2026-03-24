package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.dto.LoginResponseDTO;
import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.domain.port.PasswordEncoderPort;
import com.ortiz.tpv_backend.domain.port.TokenProviderPort;
import com.ortiz.tpv_backend.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @Mock
    private PasswordEncoderPort passwordEncoderPort;

    @Mock
    private TokenProviderPort tokenProviderPort;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    @Test
    void givenValidCredentials_whenExecute_thenReturnTokenResponse() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("admin", "admin123");
        User user = new User(1L, "admin", "encoded-password", "ROLE_ADMIN");
        LoginResponseDTO expected = LoginResponseDTO.builder()
            .token("jwt-token")
            .expiresIn(3600L)
            .user(LoginResponseDTO.UserData.builder()
                .id("1")
                .username("admin")
                .role("ROLE_ADMIN")
                .build())
            .build();

        when(userRepositoryPort.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("admin123", "encoded-password")).thenReturn(true);
        when(tokenProviderPort.generateToken(user)).thenReturn("jwt-token");

        LoginResponseDTO responseDTO = loginUseCase.execute(requestDTO);

        assertEquals(expected.getToken(), responseDTO.getToken());
        assertEquals(expected.getExpiresIn(), responseDTO.getExpiresIn());
        assertEquals(expected.getUser().getId(), responseDTO.getUser().getId());
        assertEquals(expected.getUser().getUsername(), responseDTO.getUser().getUsername());
        assertEquals(expected.getUser().getRole(), responseDTO.getUser().getRole());
    }

    @Test
    void givenUnknownUser_whenExecute_thenThrowIllegalArgument() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("unknown", "password");
        when(userRepositoryPort.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> loginUseCase.execute(requestDTO));
        verify(passwordEncoderPort, never()).matches("password", "password");
    }

    @Test
    void givenInvalidPassword_whenExecute_thenThrowIllegalArgument() {
        LoginRequestDTO requestDTO = new LoginRequestDTO("admin", "wrong");
        User user = new User(1L, "admin", "encoded-password", "ROLE_ADMIN");

        when(userRepositoryPort.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("wrong", "encoded-password")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> loginUseCase.execute(requestDTO));
        verify(tokenProviderPort, never()).generateToken(user);
    }
}

