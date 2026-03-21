package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.UserResponseDTO;
import com.ortiz.tpv_backend.domain.model.User;
import com.ortiz.tpv_backend.domain.port.UserRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetMeUseCaseImplTest {

    @Mock
    private UserRepositoryPort userRepositoryPort;

    @InjectMocks
    private GetMeUseCaseImpl getMeUseCase;

    @Test
    void givenExistingUsername_whenExecute_thenReturnUserResponse() {
        User user = new User(1L, "admin", "encoded", "ROLE_ADMIN");
        when(userRepositoryPort.findByUsername("admin")).thenReturn(Optional.of(user));

        UserResponseDTO responseDTO = getMeUseCase.execute("admin");

        assertEquals(1L, responseDTO.id());
        assertEquals("admin", responseDTO.username());
        assertEquals("ROLE_ADMIN", responseDTO.role());
    }

    @Test
    void givenUnknownUsername_whenExecute_thenThrowIllegalArgument() {
        when(userRepositoryPort.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> getMeUseCase.execute("missing"));
    }
}

