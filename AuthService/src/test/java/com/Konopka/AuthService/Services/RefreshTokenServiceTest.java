package com.Konopka.AuthService.Services;

import com.Konopka.AuthService.Entities.RefreshToken;
import com.Konopka.AuthService.Entities.User;
import com.Konopka.AuthService.Repositories.RefreshTokenRepository;
import com.Konopka.AuthService.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    private User testUser;
    private RefreshToken validToken;
    private RefreshToken expiredToken;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenDurationMs", 86400000L);

        testUser = User.builder()
                .id(1)
                .username("testuser")
                .password("encodedPassword")
                .build();

        validToken = new RefreshToken();
        validToken.setToken("valid-refresh-token");
        validToken.setUser(testUser);
        validToken.setExpiryDate(Instant.now().plusSeconds(3600));

        expiredToken = new RefreshToken();
        expiredToken.setToken("expired-refresh-token");
        expiredToken.setUser(testUser);
        expiredToken.setExpiryDate(Instant.now().minusSeconds(3600));
    }


    @Test
    public void createRefreshToken_refreshTokenCreated_returnRefreshToken() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(validToken);

        RefreshToken result = refreshTokenService.createRefreshToken(userId);

        assertThat(result).isNotNull();
        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getExpiryDate()).isAfter(Instant.now());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }


    @Test
    void isTokenExpired_WhenTokenIsExpired_ShouldReturnTrue() {
        assertThat(refreshTokenService.isTokenExpired(expiredToken)).isTrue();
    }

    @Test
    void isTokenExpired_WhenTokenIsValid_ShouldReturnFalse() {
        assertThat(refreshTokenService.isTokenExpired(validToken)).isFalse();
    }

    @Test
    void refreshToken_WhenTokenIsValid_ShouldReturnNewJwtAndUserId() {
        when(refreshTokenRepository.findByToken("valid-refresh-token")).thenReturn(Optional.of(validToken));
        when(jwtService.generateToken(any(User.class))).thenReturn("new-jwt-token");
        ResponseEntity<?> response = refreshTokenService.refreshToken("valid-refresh-token");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        Map<?, ?> body = (Map<?, ?>) response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("token")).isEqualTo("new-jwt-token");
        assertThat(body.get("AuthId")).isEqualTo(validToken.getUser().getId());
    }

    @Test
    void refreshToken_WhenTokenIsExpired_ShouldDeleteAndReturnBadRequest() {
        when(refreshTokenRepository.findByToken("expired-refresh-token")).thenReturn(Optional.of(expiredToken));

        ResponseEntity<?> response = refreshTokenService.refreshToken("expired-refresh-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Refresh token expired. Please login again.");
        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    void refreshToken_WhenTokenDoesNotExist_ShouldReturnBadRequest() {
        when(refreshTokenRepository.findByToken("unknown-token")).thenReturn(Optional.empty());

        ResponseEntity<?> response = refreshTokenService.refreshToken("unknown-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid refresh token.");
    }


    @Test
    void logoutUser_WithValidToken_ShouldDeleteAndReturnOk() {
        when(refreshTokenRepository.findByToken("valid-refresh-token")).thenReturn(Optional.of(validToken));

        ResponseEntity<?> response = refreshTokenService.logoutUser("valid-refresh-token");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Logged out successfully.");
        verify(refreshTokenRepository).delete(validToken);
    }

    @Test
    void logoutUser_WithNullToken_ShouldReturnBadRequest() {
        ResponseEntity<?> response = refreshTokenService.logoutUser((String) null);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Refresh token is required.");
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void logoutUser_WithBlankToken_ShouldReturnBadRequest() {
        ResponseEntity<?> response = refreshTokenService.logoutUser("   ");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Refresh token is required.");
    }

    @Test
    void logoutUser_WithInvalidToken_ShouldReturnBadRequest() {
        when(refreshTokenRepository.findByToken("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<?> response = refreshTokenService.logoutUser("nonexistent");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid refresh token.");
    }


    @Test
    void logoutUser_WithValidUser_ShouldDeleteTokenAndReturnOk() {
        when(refreshTokenRepository.findByUser(testUser)).thenReturn(Optional.of(validToken));

        ResponseEntity<?> response = refreshTokenService.logoutUser(Optional.of(testUser));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Logged out successfully.");
        verify(refreshTokenRepository).delete(validToken);
    }

    @Test
    void logoutUser_WithEmptyUser_ShouldReturnBadRequest() {
        ResponseEntity<?> response = refreshTokenService.logoutUser(Optional.empty());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("User is required.");
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void logoutUser_WhenUserHasNoToken_ShouldReturnBadRequest() {
        when(refreshTokenRepository.findByUser(testUser)).thenReturn(Optional.empty());

        ResponseEntity<?> response = refreshTokenService.logoutUser(Optional.of(testUser));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid refresh token.");
    }


    @Test
    void isTokenValid_WhenUserHasActiveToken_ShouldReturnTrue() {
        when(refreshTokenRepository.findByUser(testUser)).thenReturn(Optional.of(validToken));

        assertThat(refreshTokenService.isTokenValid(Optional.of(testUser))).isTrue();
    }

    @Test
    void isTokenValid_WhenUserHasNoToken_ShouldReturnFalse() {
        when(refreshTokenRepository.findByUser(testUser)).thenReturn(Optional.empty());

        assertThat(refreshTokenService.isTokenValid(Optional.of(testUser))).isFalse();
    }

    @Test
    void isTokenValid_WhenUserIsEmpty_ShouldReturnFalse() {
        assertThat(refreshTokenService.isTokenValid(Optional.empty())).isFalse();
        verifyNoInteractions(refreshTokenRepository);
    }

}