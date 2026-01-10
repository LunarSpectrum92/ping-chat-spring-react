package com.Konopka.AuthService.Services;


import com.Konopka.AuthService.DTO.AuthenticationResponseDto;
import com.Konopka.AuthService.DTO.UserDto;
import com.Konopka.AuthService.DTO.UserServiceUserDto;
import com.Konopka.AuthService.Entities.RefreshToken;
import com.Konopka.AuthService.Entities.User;
import com.Konopka.AuthService.Feign.UserFeign;
import com.Konopka.AuthService.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserFeign userFeign;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, UserFeign userFeign, BCryptPasswordEncoder bCryptPasswordEncoder, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userFeign = userFeign;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.refreshTokenService = refreshTokenService;
    }




    public boolean registerUser(UserDto userDto) {
        System.out.println(userDto.toString());
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists: " + userDto.username());
        }

        String encodedPassword = bCryptPasswordEncoder.encode(userDto.password());

        User user = User.builder()
                .username(userDto.username())
                .password(encodedPassword)
                .build();

        User savedUser = userRepository.save(user);

        try {
            UserServiceUserDto userServiceUserDto = new UserServiceUserDto(
                    savedUser.getId(),
                    userDto.firstName(),
                    userDto.lastName()
            );
            userFeign.createUser(userServiceUserDto, savedUser.getId());
        } catch (Exception e) {
            userRepository.delete(user);
            throw new RuntimeException("Failed to create user in external service", e);
        }

        return true;
    }



    public AuthenticationResponseDto logIn(UserDto userDto) {
        Optional<User> user = userRepository.findByUsername(userDto.username());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("username not found");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.username(),
                        userDto.password()
                ));
        if(authentication.isAuthenticated()) {
            if(refreshTokenService.isTokenValid(user)) {
                refreshTokenService.logoutUser(user);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.get().getId());
            return new AuthenticationResponseDto(refreshToken, jwtService.generateToken(user.get()), user.get().getId());
        }else  {
            throw new BadCredentialsException("Bad credentials");
        }
    }

}
