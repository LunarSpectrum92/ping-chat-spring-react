package com.konopka.UserService.Services;

import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.Invitation;
import com.konopka.UserService.Entities.User;
import com.konopka.UserService.Repositories.InvitationRepostitory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvitationServiceTest {

    @Mock
    private InvitationRepostitory invitationRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private InvitationService invitationService;

    private User sender;
    private User receiver;
    private InvitationDto invitationDto;

    @BeforeEach
    void setUp() {
        sender = User.builder().id(1).authId(1).firstName("Jan").lastName("Kowalski").build();
        receiver = User.builder().id(2).authId(2).firstName("Anna").lastName("Nowak").build();
        invitationDto = new InvitationDto(1, 2);
    }

    @Test
    void sendInvitation_SameSenderAndReceiver_ReturnsBadRequest() {
        InvitationDto selfInvite = new InvitationDto(1, 1);

        ResponseEntity<InvitationDto> result = invitationService.sendInvitation(selfInvite, 1, 1);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verifyNoInteractions(invitationRepository);
    }

    @Test
    void sendInvitation_UsersAreAlreadyFriends_ReturnsBadRequest() {
        when(userService.areFriends(1, 2)).thenReturn(true);

        ResponseEntity<InvitationDto> result = invitationService.sendInvitation(invitationDto, 1, 2);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(invitationRepository, never()).save(any());
    }

    @Test
    void sendInvitation_InvitationAlreadyExists_ReturnsConflict() {
        when(userService.areFriends(1, 2)).thenReturn(false);
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2))
                .thenReturn(Optional.of(new Invitation()));

        ResponseEntity<InvitationDto> result = invitationService.sendInvitation(invitationDto, 1, 2);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        verify(invitationRepository, never()).save(any());
    }

    @Test
    void sendInvitation_ValidInvitation_SavesAndReturnsOk() {
        when(userService.areFriends(1, 2)).thenReturn(false);
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(Optional.empty());
        when(userService.getUserById(1)).thenReturn(sender);
        when(userService.getUserById(2)).thenReturn(receiver);

        ResponseEntity<InvitationDto> result = invitationService.sendInvitation(invitationDto, 1, 2);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(invitationDto);
        verify(invitationRepository).save(any(Invitation.class));
    }

    @Test
    void acceptInvitation_InvitationDoesNotExist_ReturnsNotFound() {
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(Optional.empty());

        ResponseEntity<InvitationDto> result = invitationService.acceptInvitation(invitationDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, never()).addFriend(any());
    }

    @Test
    void acceptInvitation_InvitationExists_AddsFriendAndDeletesInvitation() {
        Invitation invitation = new Invitation();
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2))
                .thenReturn(Optional.of(invitation));

        ResponseEntity<InvitationDto> result = invitationService.acceptInvitation(invitationDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(invitationDto);
        verify(userService).addFriend(invitationDto);
        verify(invitationRepository).delete(invitation);
    }

    @Test
    void declineInvitation_InvitationDoesNotExist_ReturnsNotFound() {
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(Optional.empty());

        ResponseEntity<InvitationDto> result = invitationService.declineInvitation(invitationDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(invitationRepository, never()).delete(any());
    }

    @Test
    void declineInvitation_InvitationExists_DeletesInvitationAndReturnsOk() {
        Invitation invitation = new Invitation();
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2))
                .thenReturn(Optional.of(invitation));

        ResponseEntity<InvitationDto> result = invitationService.declineInvitation(invitationDto);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo(invitationDto);
        verify(invitationRepository).delete(invitation);
    }

    @Test
    void getInvitations_NoInvitationsFound_ReturnsNotFound() {
        when(userService.getUserById(2)).thenReturn(receiver);
        when(invitationRepository.findByReceiver(receiver)).thenReturn(List.of());

        ResponseEntity<List<UserDto>> result = invitationService.GetInvitations(2);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getInvitations_InvitationsExist_ReturnsListOfSenders() {
        Invitation invitation = Invitation.builder().sender(sender).receiver(receiver).build();
        when(userService.getUserById(2)).thenReturn(receiver);
        when(invitationRepository.findByReceiver(receiver)).thenReturn(List.of(invitation));

        ResponseEntity<List<UserDto>> result = invitationService.GetInvitations(2);

        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).hasSize(1);
        assertThat(result.getBody().get(0).firstName()).isEqualTo("Jan");
        assertThat(result.getBody().get(0).lastName()).isEqualTo("Kowalski");
    }

    @Test
    void doesInvitationExist_NullSenderId_ReturnsFalse() {
        boolean result = invitationService.doesInvitationExist(null, 2);

        assertThat(result).isFalse();
        verifyNoInteractions(invitationRepository);
    }

    @Test
    void doesInvitationExist_NullReceiverId_ReturnsFalse() {
        boolean result = invitationService.doesInvitationExist(1, null);

        assertThat(result).isFalse();
        verifyNoInteractions(invitationRepository);
    }

    @Test
    void doesInvitationExist_NoInvitationInEitherDirection_ReturnsTrue() {
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(Optional.empty());
        when(invitationRepository.findBySender_IdAndReceiver_Id(2, 1)).thenReturn(Optional.empty());

        boolean result = invitationService.doesInvitationExist(1, 2);

        assertThat(result).isTrue();
    }

    @Test
    void doesInvitationExist_DirectInvitationExists_ReturnsFalse() {
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2))
                .thenReturn(Optional.of(new Invitation()));
        when(invitationRepository.findBySender_IdAndReceiver_Id(2, 1)).thenReturn(Optional.empty());

        boolean result = invitationService.doesInvitationExist(1, 2);

        assertThat(result).isFalse();
    }

    @Test
    void doesInvitationExist_ReverseInvitationExists_ReturnsFalse() {
        when(invitationRepository.findBySender_IdAndReceiver_Id(1, 2)).thenReturn(Optional.empty());
        when(invitationRepository.findBySender_IdAndReceiver_Id(2, 1))
                .thenReturn(Optional.of(new Invitation()));

        boolean result = invitationService.doesInvitationExist(1, 2);

        assertThat(result).isFalse();
    }
}