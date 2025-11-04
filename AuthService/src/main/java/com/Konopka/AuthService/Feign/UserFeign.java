package com.Konopka.AuthService.Feign;


import com.Konopka.AuthService.DTO.UserServiceUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "UserService",
        url = "http://localhost:8082/user")
public interface UserFeign {

    @PostMapping("/create")
    ResponseEntity<UserServiceUserDto> createUser(@RequestBody UserServiceUserDto userServiceUserDto);

}


