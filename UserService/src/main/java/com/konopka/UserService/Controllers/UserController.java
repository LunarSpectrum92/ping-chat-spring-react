package com.konopka.UserService.Controllers;

import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.User;
import com.konopka.UserService.Repositories.UserRepository;
import com.konopka.UserService.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<UserDto> getUser(@PathVariable int authId, Principal principal) {
        System.out.println(principal.getName());
        return userService.getUser(authId);
    }

    //find all users
    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers(Principal principal) {
        return userService.getAllUsers(Integer.valueOf(principal.getName()));
    }
    //chat field
        //find friends
    @GetMapping("/friends")
    public ResponseEntity<List<UserDto>> getFriends(Principal principal) {
        return userService.getFriends(Integer.parseInt(principal.getName()));
    }
        //find friend



}
