package com.Konopka.AuthService.Services;


import com.Konopka.AuthService.DTO.UserDto;
import com.Konopka.AuthService.Entities.User;
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

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Autowired
    public UserService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }



    public boolean registerUser(UserDto userDto) {

        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            return false;
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(userDto.password());

        User user = User.builder()
                .username(userDto.username())
                .password(encodedPassword)
                .build();

        try{
            userRepository.save(user);
        }catch(Exception e){
            throw new UsernameNotFoundException("username not found" + userDto.username());
        }

        return true;
    }


    public String logIn(UserDto userDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDto.username(),
                        userDto.password()
                ));
        if(authentication.isAuthenticated()) {
            return jwtService.generateToken(userDto);
        }else  {
            throw new BadCredentialsException("Bad credentials");
        }
    }

}
