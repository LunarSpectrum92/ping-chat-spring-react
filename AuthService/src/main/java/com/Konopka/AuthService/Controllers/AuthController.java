package com.Konopka.AuthService.Controllers;


import com.Konopka.AuthService.DTO.UserDto;
import com.Konopka.AuthService.Entities.User;
import com.Konopka.AuthService.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController{

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public boolean RegisterUser(@RequestBody UserDto user){
        return userService.registerUser(user);
    }


    @GetMapping("/login")
    public String login(@RequestBody UserDto user){
        return userService.logIn(user);
    }



}
