package com.konopka.UserService.Controllers;

import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.User;
import com.konopka.UserService.Repositories.UserRepository;
import com.konopka.UserService.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return userService.CreateUser(userDto);
    }


    @GetMapping("/get/{authId}")
    @PreAuthorize("#authId.toString() == authentication.principal")
    public ResponseEntity<UserDto> getUser(@PathVariable int authId) {
        return userService.getUser(authId);
    }




}
