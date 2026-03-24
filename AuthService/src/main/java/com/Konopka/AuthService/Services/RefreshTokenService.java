package com.Konopka.AuthService.Services;

import com.Konopka.AuthService.Entities.RefreshToken;
import com.Konopka.AuthService.Entities.User;
import com.Konopka.AuthService.Repositories.RefreshTokenRepository;
import com.Konopka.AuthService.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${jwt.refreshExpirationMs}")
    private Long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public RefreshTokenService(RefreshTokenRepository repo, UserRepository userRepo, JwtService jwtService) {
        this.refreshTokenRepository = repo;
        this.userRepository = userRepo;
        this.jwtService = jwtService;
    }

    public RefreshToken createRefreshToken(int userId) {
        RefreshToken token = new RefreshToken();
        token.setUser(userRepository.findById(userId).get());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        token.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(token);
    }

    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }

    public ResponseEntity<?> refreshToken(@RequestBody String refreshToken){
        return refreshTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    if (this.isTokenExpired(token)) {
                        refreshTokenRepository.delete(token);
                        return ResponseEntity.badRequest().body("Refresh token expired. Please login again.");
                    }
                    String newJwt = jwtService.generateToken(User.builder()
                                    .id(token.getUser().getId())
                                    .username(token.getUser().getUsername())
                                    .password(token.getUser().getPassword())
                                    .build());
                    return ResponseEntity.ok(Map.of("token", newJwt, "AuthId", token.getUser().getId()));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }


    public ResponseEntity<?> logoutUser(@RequestBody String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required.");
        }
        return refreshTokenRepository.findByToken(refreshToken)
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }

    public ResponseEntity<?> logoutUser(Optional<User> user) {
        if(user.isEmpty()) {
            return ResponseEntity.badRequest().body("User is required.");
        }
        return refreshTokenRepository.findByUser(user.get())
                .map(token -> {
                    refreshTokenRepository.delete(token);
                    return ResponseEntity.ok("Logged out successfully.");
                })
                .orElse(ResponseEntity.badRequest().body("Invalid refresh token."));
    }

    public Boolean isTokenValid(Optional<User> user) {
        return user.filter(value -> refreshTokenRepository.findByUser(value).isPresent()).isPresent();
    }

}