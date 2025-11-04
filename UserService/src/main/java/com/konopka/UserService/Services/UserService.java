package com.konopka.UserService.Services;


import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.Invitation;
import com.konopka.UserService.Entities.User;
import com.konopka.UserService.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }




    public ResponseEntity<UserDto> CreateUser(UserDto userDto) {

        if(userRepository.findByAuthId(userDto.AuthId()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        User user = User.builder()
                .authId(userDto.AuthId())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .build();

        try {
            userRepository.save(user);
        }catch (Exception e) {
            throw new RuntimeException("Problem while saving user: " + e.getMessage());
        }

        return ResponseEntity.ok(userDto);
    }



    public ResponseEntity<UserDto> AddFriend(UserDto userDto) {
        if(userRepository.findByAuthId(userDto.AuthId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        return ResponseEntity.ok(userDto);
    }



    public ResponseEntity<UserDto> getUser(int authId) {
        var user = userRepository.findByAuthId(authId);
        UserDto userDto = new UserDto(user.get().getAuthId(), user.get().getFirstName(), user.get().getLastName());
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(userDto);
    }


    public User getUserById(int userId) {
        var user = userRepository.findById(userId);
        if(user.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return user.get();
    }

    @Transactional
    public ResponseEntity<InvitationDto> addFriend(InvitationDto invitationDto) {
        Optional<User> sender = userRepository.findById(invitationDto.senderId());
        Optional<User> receiver = userRepository.findById(invitationDto.reciverId());
        if(sender.isEmpty() || receiver.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        sender.get().addFriend(receiver.get());
        userRepository.save(sender.get());
        return ResponseEntity.ok(invitationDto);
    }

    public ResponseEntity<List<UserDto>> getFriends(int authId) {
        Optional<User> user = userRepository.findByAuthId(authId);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<User> friends = user.get().getFriends();
        List<UserDto> userDtos = new ArrayList<>();
        for(User u :  friends) {
            userDtos.add(new UserDto(u.getAuthId(), u.getFirstName(), u.getLastName()));
        }
        return ResponseEntity.ok(userDtos);
    }


    public ResponseEntity<List<Invitation>> getReceivedInvitations(int AuthId) {

        var user = userRepository.findByAuthId(AuthId);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        List<Invitation> invitations = user.get().getReceivedInvitations();

        return ResponseEntity.ok(invitations);
    }


}
