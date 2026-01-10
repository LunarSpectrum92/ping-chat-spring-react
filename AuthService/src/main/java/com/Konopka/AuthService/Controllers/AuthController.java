package com.Konopka.AuthService.Controllers;


import com.Konopka.AuthService.DTO.ResponseUserDto;
import com.Konopka.AuthService.DTO.UserDto;
import com.Konopka.AuthService.Entities.User;
import com.Konopka.AuthService.Repositories.RefreshTokenRepository;
import com.Konopka.AuthService.Services.RefreshTokenService;
import com.Konopka.AuthService.Services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController{

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, RefreshTokenRepository refreshTokenRepository, RefreshTokenService refreshTokenService, RefreshTokenService refreshTokenService1) {
        this.userService = userService;
        this.refreshTokenService = refreshTokenService1;
    }


    @PostMapping("/register")
    public boolean RegisterUser(@RequestBody UserDto user){
        return userService.registerUser(user);
    }


    @PostMapping("/login")
    public ResponseEntity<ResponseUserDto> login(@RequestBody UserDto user) {
        var tokens = userService.logIn(user);
        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", tokens.refreshToken().getToken())
                .httpOnly(true)
                .build();
        var responseUserDto = new ResponseUserDto(tokens.accessToken(), tokens.AuthId());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                //tokens.accessToken()
                .body(responseUserDto);
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value="refreshToken") String refreshToken) {
        return refreshTokenService.refreshToken(refreshToken);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@CookieValue(value="refreshToken") String refreshToken) {
        return refreshTokenService.logoutUser(refreshToken);
    }

}
