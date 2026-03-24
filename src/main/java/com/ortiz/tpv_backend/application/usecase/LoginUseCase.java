package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;
import com.ortiz.tpv_backend.application.dto.LoginResponseDTO; // 1. Importamos el nuevo DTO anidado

public interface LoginUseCase {

    // 2. Cambiamos el tipo de retorno a LoginResponseDTO
    LoginResponseDTO execute(LoginRequestDTO requestDTO);
}