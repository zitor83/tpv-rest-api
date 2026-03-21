package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.AuthResponseDTO;
import com.ortiz.tpv_backend.application.dto.LoginRequestDTO;

public interface LoginUseCase {

    AuthResponseDTO execute(LoginRequestDTO requestDTO);
}

