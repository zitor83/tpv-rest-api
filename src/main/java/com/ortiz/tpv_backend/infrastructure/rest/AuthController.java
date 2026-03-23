package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.AuthResponseDTO;
import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.dto.UserResponseDTO;
import com.ortiz.tpv_backend.application.usecase.GetMeUseCase;
import com.ortiz.tpv_backend.application.usecase.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Gestiona la autenticacion de usuarios y la validacion de sesion basada en JWT.")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final GetMeUseCase getMeUseCase;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Valida las credenciales del usuario (username o email y password) y devuelve un JWT para consumir endpoints protegidos. Si las credenciales no son validas, responde con error de autenticacion.",
        tags = {"Authentication"}
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Autenticación correcta. JWT emitido.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Validación fallida: username/password faltantes o credenciales inválidas",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(
        @Valid @RequestBody LoginRequestDTO requestDTO
    ) {
        AuthResponseDTO authResponseDTO = loginUseCase.execute(requestDTO);
        ApiResponse<AuthResponseDTO> response = ApiResponse.success("Autenticación correcta", authResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(
        summary = "Validar sesión actual",
        description = "Obtiene el perfil del usuario autenticado a partir del JWT enviado en Authorization Bearer. Permite reconstruir el estado de sesion sin volver a solicitar credenciales.",
        tags = {"Authentication"}
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Usuario recuperado correctamente del token JWT",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "401",
            description = "Token ausente, inválido o expirado",
            content = @Content(mediaType = "application/json")
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "403",
            description = "Token válido pero usuario sin permisos",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<ApiResponse<UserResponseDTO>> getMe(
        Authentication authentication
    ) {
        UserResponseDTO userDTO = getMeUseCase.execute(authentication.getName());
        ApiResponse<UserResponseDTO> response = ApiResponse.success("Usuario recuperado correctamente", userDTO);
        return ResponseEntity.ok(response);
    }
}

