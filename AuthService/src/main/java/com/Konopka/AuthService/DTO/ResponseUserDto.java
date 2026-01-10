package com.Konopka.AuthService.DTO;

public record ResponseUserDto(
        String token,
        Integer AuthId
) {
}
