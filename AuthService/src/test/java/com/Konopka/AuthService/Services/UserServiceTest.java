package com.Konopka.AuthService.Services;


import com.Konopka.AuthService.DTO.AuthenticationResponseDto;
import com.Konopka.AuthService.DTO.UserDto;
import com.Konopka.AuthService.Entities.RefreshToken;
import com.Konopka.AuthService.Entities.User;
import com.Konopka.AuthService.Feign.UserFeign;
import com.Konopka.AuthService.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;
    @Mock
    private UserFeign userFeign;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;



    @Test
    void registerUser_successfullyRegisterUser_returnTrue() {
        UserDto userDto = new UserDto("password", "kowalski15", "Jan", "Kowalski");
        User savedUser = User.builder()
                .id(1)
                .password("password")
                .username("kowlski15")
                .build();

        when(userRepository.findByUsername("kowalski15")).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        boolean result = userService.registerUser(userDto);

        assertTrue(result);
        verify(userFeign, times(1)).createUser(any(), eq(1));
        verify(userRepository).save(any());
    }



    @Test
    void registerUser_userAlreadyExists_throwsIllegalArgumentException() {
        UserDto userDto = new UserDto("password", "kowalski15", "Jan", "Kowalski");
        User savedUser = User.builder()
                .id(1)
                .password("password")
                .username("kowlski15")
                .build();

        when(userRepository.findByUsername("kowalski15")).thenReturn(Optional.of(savedUser));


        assertThatThrownBy(() -> userService.registerUser(userDto)).isInstanceOf(IllegalArgumentException.class);

        verify(userFeign, never()).createUser(any(), eq(1));
        verify(bCryptPasswordEncoder, never()).encode(userDto.password());
        verify(userRepository, never()).save(any());
    }


    @Test
    void logIn_userLoggedInSuccessfully_returnAuthenticationResponseDto() {
        UserDto userDto = new UserDto("password", "kowalski15", "Jan", "Kowalski");
        User savedUser = User.builder().id(1).username("kowalski15").password("password").build();
        RefreshToken mockRefreshToken = new RefreshToken();

        when(userRepository.findByUsername("kowalski15")).thenReturn(Optional.of(savedUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(refreshTokenService.isTokenValid(Optional.of(savedUser))).thenReturn(false);
        when(refreshTokenService.createRefreshToken(1)).thenReturn(mockRefreshToken);
        when(jwtService.generateToken(savedUser)).thenReturn("token");


        AuthenticationResponseDto response = userService.logIn(userDto);


        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("token");
        assertThat(response.refreshToken()).isEqualTo(mockRefreshToken);
        assertThat(response.AuthId()).isEqualTo(1);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(refreshTokenService).isTokenValid(Optional.of(savedUser));
        verify(refreshTokenService).createRefreshToken(1);
        verify(jwtService).generateToken(savedUser);
    }


    @Test
    void logIn_userNotFound_throwsUsernameNotFoundException() {
        UserDto userDto = new UserDto("password", "ghost99", "Jan", "Kowalski");

        when(userRepository.findByUsername("ghost99")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.logIn(userDto))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("username not found");

        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
        verifyNoInteractions(refreshTokenService);
    }


    @Test
    void logIn_authenticationFails_throwsBadCredentialsException() {
        UserDto userDto = new UserDto("wrongPass", "kowalski15", "Jan", "Kowalski");
        User savedUser = User.builder().id(1).username("kowalski15").password("password").build();

        when(userRepository.findByUsername("kowalski15")).thenReturn(Optional.of(savedUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);

        assertThatThrownBy(() -> userService.logIn(userDto))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessage("Bad credentials");

        verifyNoInteractions(jwtService);
        verifyNoInteractions(refreshTokenService);
    }


    @Test
    void logIn_existingTokenValid_logsOutFirstThenReturnsNewTokens() {
        UserDto userDto = new UserDto("password", "kowalski15", "Jan", "Kowalski");
        User savedUser = User.builder().id(1).username("kowalski15").password("password").build();
        RefreshToken newRefreshToken = new RefreshToken();

        when(userRepository.findByUsername("kowalski15")).thenReturn(Optional.of(savedUser));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(refreshTokenService.isTokenValid(Optional.of(savedUser))).thenReturn(true); // <-- token already exists
        when(refreshTokenService.createRefreshToken(1)).thenReturn(newRefreshToken);
        when(jwtService.generateToken(savedUser)).thenReturn("token");

        AuthenticationResponseDto response = userService.logIn(userDto);

        InOrder order = inOrder(refreshTokenService);
        order.verify(refreshTokenService).logoutUser(Optional.of(savedUser));
        order.verify(refreshTokenService).createRefreshToken(1);

        assertThat(response).isNotNull();
        assertThat(response.accessToken()).isEqualTo("token");
        assertThat(response.refreshToken()).isEqualTo(newRefreshToken);
        assertThat(response.AuthId()).isEqualTo(1);
    }

    








}
