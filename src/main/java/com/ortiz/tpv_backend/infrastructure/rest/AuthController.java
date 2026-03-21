package com.ortiz.tpv_backend.infrastructure.rest;

import com.ortiz.tpv_backend.application.dto.AuthResponseDTO;
import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.usecase.LoginUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
}

