package com.Konopka.AuthService.DTO;

public record UserDto(
        String password,
        String username,
        String firstName,
        String lastName
) {
}
