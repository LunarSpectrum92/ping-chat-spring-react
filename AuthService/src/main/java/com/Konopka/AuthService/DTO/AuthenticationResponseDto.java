package com.Konopka.AuthService.DTO;

import com.Konopka.AuthService.Entities.RefreshToken;

public record AuthenticationResponseDto(
        RefreshToken refreshToken,
        String accessToken,
        Integer AuthId
) {}
