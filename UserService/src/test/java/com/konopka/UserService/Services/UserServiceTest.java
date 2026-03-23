package com.konopka.UserService.Services;


import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.User;
import com.konopka.UserService.Repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;


    @InjectMocks
    @Spy
    private UserService userService;


    //createUser
    @Test
    public void createUser_userCreatedSuccessfully_returnResponseEntityOk(){

        UserDto userDto = new UserDto(
                1,
                "Wojtek",
                "Kowalski"
        );
        when(userRepository.findByAuthId(userDto.AuthId())).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = userService.CreateUser(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(userDto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getFirstName()).isEqualTo("Wojtek");
        assertThat(capturedUser.getAuthId()).isEqualTo(1);
    }


    @Test
    public void createUser_userNotCreatedSuccessfully_returnResponseEntityConflict() {

        UserDto userDto = new UserDto(1, "Wojtek", "Kowalski");
        when(userRepository.findByAuthId(userDto.AuthId())).thenReturn(Optional.of(new User()));

        ResponseEntity<UserDto> response = userService.CreateUser(userDto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    public void createUser_userNotSavedSuccessfully_throwNewRuntimeException() {

        UserDto userDto = new UserDto(1, "Wojtek", "Kowalski");
        when(userRepository.findByAuthId(userDto.AuthId())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException());

        assertThatThrownBy(() -> userService.CreateUser(userDto)).isInstanceOf(RuntimeException.class);
    }

    //getUserByAuthId
    @Test
    public void getUserByAuthId_findsUserCorrectly_returnUserId(){
        Integer authId = 1;
        when(userRepository.findByAuthId(authId)).thenReturn(Optional.of(User.builder().id(authId).build()));

        Integer response = userService.getUserIdByAuthId(authId);

        assertThat(response).isEqualTo(authId);
    }


    @Test
    public void getUserByAuthId_userIsEmpty_throwsRuntimeException() {
        Integer authId = 1;
        when(userRepository.findByAuthId(authId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserIdByAuthId(authId)).isInstanceOf(RuntimeException.class).hasMessage("User not found");
    }

    //getUserById
    @Test
    public void getUserById_userFoundCorrectly_returnUser(){
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().id(userId).build()));

        User response = userService.getUserById(userId);

        assertThat(response).isEqualTo(User.builder().id(userId).build());
        verify(userRepository).findById(userId);
    }


    @Test
    public void getUserById_userNotFound_throwsRuntimeException() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId)).isInstanceOf(RuntimeException.class).hasMessage("User not found");
    }

    //getUser
    @Test
    public void getUser_userFoundCorrectly_returnResponseEntityOkWithUserDto() {
        int authId = 1;
        User user = User.builder()
                .authId(authId)
                .firstName("Jan")
                .lastName("Kowalski")
                .build();

        when(userRepository.findByAuthId(authId)).thenReturn(Optional.of(user));

        ResponseEntity<UserDto> response = userService.getUser(authId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().AuthId()).isEqualTo(authId);
        assertThat(response.getBody().firstName()).isEqualTo("Jan");
        verify(userRepository).findByAuthId(authId);
    }


    @Test
    public void getUser_userIsEmpty_returnResponseEntityOkWithUserDto() {
        int authId = 1;

        when(userRepository.findByAuthId(authId)).thenReturn(Optional.empty());

        ResponseEntity<UserDto> response = userService.getUser(authId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(userRepository).findByAuthId(authId);
    }

    //addFriend
    @Test
    public void addFriend_friendAddedSuccessfully_returnResponseEntityOkWithInvitationDto() {
        User sender = User.builder().id(1).friends(new ArrayList<>()).build();
        User receiver = User.builder().id(2).friends(new ArrayList<>()).build();
        InvitationDto dto = new InvitationDto(1, 2);

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));

        ResponseEntity<InvitationDto> invitationDtoResponseEntity = userService.addFriend(dto);

        assertThat(invitationDtoResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(invitationDtoResponseEntity.getBody()).isEqualTo(dto);
        assertThat(sender.getFriends()).contains(receiver);
        assertThat(receiver.getFriends()).contains(sender);
        verify(userRepository).findById(1);
        verify(userRepository).findById(2);
    }


    @Test
    public void addFriend_senderIsEmpty_returnResponseEntityNotFound() {
        InvitationDto dto = new InvitationDto(1, 2);
        User sender = User.builder().id(1).friends(new ArrayList<>()).build();
        User receiver = User.builder().id(2).friends(new ArrayList<>()).build();
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));

        var response = userService.addFriend(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(receiver.getFriends()).isEmpty();
        verify(userRepository).findById(1);
        verify(userRepository).findById(2);
    }


    @Test
    public void addFriend_receiverAndSenderAreEqual_returnResponseEntityBadRequest() {
        InvitationDto dto = new InvitationDto(1, 1);
        User sender = User.builder().id(1).friends(new ArrayList<>()).build();
        User receiver = User.builder().id(1).friends(new ArrayList<>()).build();
        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(1)).thenReturn(Optional.of(receiver));

        var response = userService.addFriend(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }


    //getFriends
    @Test
    public void getFriends_friendsReturnedCorrectly_returnResponseEntityOkWithUserDtoList() {
        int authId = 1;
        Optional<User> user = Optional.of(
                User.builder().authId(authId).friends(
                        new ArrayList<>(List.of(
                                User.builder().authId(2).build(), User.builder().authId(3).build())
                        )).build());
        when(userRepository.findByAuthId(authId)).thenReturn(user);

        var response = userService.getFriends(authId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody().get(1).AuthId()).isEqualTo(3);
        verify(userRepository).findByAuthId(authId);
    }


    @Test
    public void getFriends_friendsAreNotPresent_returnResponseEntityNotFound() {
        int authId = 1;
        when(userRepository.findByAuthId(authId)).thenReturn(Optional.empty());

        var response = userService.getFriends(authId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userRepository).findByAuthId(authId);
    }

    //getAllUsersExceptSelf
    @Test
    public void getAllUsersExceptSelf_usersExceptSelfReturnedCorrectly_returnResponseEntityOkWithUserDtoList() {
        int authId = 1;
        Optional<User> user = Optional.of(User.builder().authId(1).build());
        List<User> userList = List.of(User.builder().authId(2).build(), User.builder().authId(3).build(), User.builder().authId(4).build());
        when(userRepository.findAllByAuthIdNot(authId)).thenReturn(userList);
        when(userService.areFriends(2, 1)).thenReturn(true);
        when(userService.areFriends(3, 1)).thenReturn(false);
        when(userService.areFriends(4, 1)).thenReturn(false);

        var response = userService.getAllUsersExceptSelf(authId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().size()).isEqualTo(2);
        for(var l : response.getBody()){
            assertThat(l.AuthId()).isNotEqualTo(1);
        }
        verify(userRepository).findAllByAuthIdNot(authId);
    }


    @Test
    public void getAllUsersExceptSelf_usersAreEmpty_returnResponseEntityNotFound() {
        int authId = 1;
        List<User> userList = new ArrayList<>();
        when(userRepository.findAllByAuthIdNot(authId)).thenReturn(userList);

        ResponseEntity<List<UserDto>> response = userService.getAllUsersExceptSelf(authId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(userRepository).findAllByAuthIdNot(authId);
    }



}
