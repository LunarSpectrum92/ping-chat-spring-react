package com.konopka.UserService.Services;


import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.Invitation;
import com.konopka.UserService.Repositories.InvitationRepostitory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvitationService {

    private final InvitationRepostitory invitationRepository;
    private final UserService userService;

    public InvitationService(InvitationRepostitory invitationRepostitory, UserService userService) {
        this.invitationRepository = invitationRepostitory;
        this.userService = userService;
    }


    //TODO
    //sendInvitation
    //acceptInvitation
    //getInvitations
    //rejectInvitation

    public ResponseEntity<InvitationDto> sendInvitation(InvitationDto invitationDto) {
        Optional<Invitation> invitation = invitationRepository.findBySender_IdAndReceiver_Id(invitationDto.senderId(), invitationDto.reciverId());
        if(invitation.isPresent() ) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Invitation invitationToSave = Invitation.builder()
                .sender(userService.getUserById(invitationDto.senderId()))
                .receiver(userService.getUserById(invitationDto.reciverId()))
                .build();
        invitationRepository.save(invitationToSave);
        return ResponseEntity.ok(invitationDto);
    }

    public ResponseEntity<InvitationDto> acceptInvitation(InvitationDto invitationDto) {
        Optional<Invitation> invitation = invitationRepository.findBySender_IdAndReceiver_Id(invitationDto.senderId(), invitationDto.reciverId());
        if(invitation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.addFriend(invitationDto);
        invitationRepository.delete(invitation.get());

        return ResponseEntity.ok(invitationDto);
    }

    public ResponseEntity<Invitation> declineInvitation(int invitationId) {
        Optional<Invitation> invitation = invitationRepository.findById(invitationId);
        if(invitation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        invitationRepository.delete(invitation.get());

        return ResponseEntity.ok(invitation.get());
    }







}
