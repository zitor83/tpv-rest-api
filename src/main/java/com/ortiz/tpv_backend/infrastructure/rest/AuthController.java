package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.dto.LoginResponseDTO; // Nuevo DTO con estructura anidada
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
@Tag(name = "Authentication", description = "Gestiona la autenticación de usuarios y la validación de sesión basada en JWT.")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final GetMeUseCase getMeUseCase;

    @PostMapping("/login")
    @Operation(
            summary = "Iniciar sesión",
            description = "Valida las credenciales y devuelve un JWT junto con el perfil del usuario y tiempo de expiración.",
            tags = {"Authentication"}
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Autenticación correcta. JWT emitido con el formato solicitado por el frontend.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validación fallida: credenciales inválidas o incompletas.",
                    content = @Content(mediaType = "application/json")
            )
    })
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO requestDTO
    ) {
        // El LoginUseCase ahora debe retornar el objeto LoginResponseDTO con la estructura de datos anidada
        LoginResponseDTO loginResponse = loginUseCase.execute(requestDTO);
        ApiResponse<LoginResponseDTO> response = ApiResponse.success("Autenticación correcta", loginResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Validar sesión actual",
            description = "Obtiene el perfil del usuario autenticado a partir del JWT enviado en la cabecera.",
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