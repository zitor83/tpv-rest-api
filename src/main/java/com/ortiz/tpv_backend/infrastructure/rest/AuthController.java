package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.AuthResponseDTO;
import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.dto.UserResponseDTO;
import com.ortiz.tpv_backend.application.usecase.GetMeUseCase;
import com.ortiz.tpv_backend.application.usecase.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Endpoints de autenticacion")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final GetMeUseCase getMeUseCase;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Autentica usuario y devuelve JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autenticacion correcta",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Credenciales invalidas o payload invalido", content = @Content)
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<AuthResponseDTO>> login(
        @Valid @RequestBody LoginRequestDTO requestDTO
    ) {
        AuthResponseDTO authResponseDTO = loginUseCase.execute(requestDTO);
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<AuthResponseDTO> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Autenticacion correcta", authResponseDTO);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    @Operation(summary = "Sesion actual", description = "Devuelve los datos del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario recuperado correctamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "Token ausente o invalido", content = @Content),
        @ApiResponse(responseCode = "403", description = "Acceso denegado", content = @Content)
    })
    public ResponseEntity<com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<UserResponseDTO>> getMe(
        Authentication authentication
    ) {
        UserResponseDTO userDTO = getMeUseCase.execute(authentication.getName());
        com.ortiz.tpv_backend.infrastructure.rest.ApiResponse<UserResponseDTO> response =
            com.ortiz.tpv_backend.infrastructure.rest.ApiResponse.success("Usuario recuperado correctamente", userDTO);

        return ResponseEntity.ok(response);
    }
}

