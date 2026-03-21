package com.ortiz.tpv_backend.application.usecase;

import com.ortiz.tpv_backend.application.dto.UserResponseDTO;

public interface GetMeUseCase {

    UserResponseDTO execute(String username);
}

