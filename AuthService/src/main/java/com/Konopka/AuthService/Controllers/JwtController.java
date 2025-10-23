package com.Konopka.AuthService.Controllers;


import com.Konopka.AuthService.Services.JwtService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/jwt")
public class JwtController {

    private final JwtService jwtService;


    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }


}
