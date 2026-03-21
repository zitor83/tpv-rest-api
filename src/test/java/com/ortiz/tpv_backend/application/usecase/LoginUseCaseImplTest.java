package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.AuthResponseDTO;
import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

        when(userRepositoryPort.findByUsername("admin")).thenReturn(Optional.of(user));
        when(passwordEncoderPort.matches("admin123", "encoded-password")).thenReturn(true);
        when(tokenProviderPort.generateToken(user)).thenReturn("jwt-token");

        AuthResponseDTO responseDTO = loginUseCase.execute(requestDTO);

        assertEquals("jwt-token", responseDTO.token());
        assertEquals("admin", responseDTO.username());
        assertEquals("ROLE_ADMIN", responseDTO.role());
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

