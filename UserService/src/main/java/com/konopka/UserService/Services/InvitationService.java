package com.konopka.UserService.Services;


import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Entities.Invitation;
import com.konopka.UserService.Repositories.InvitationRepostitory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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


    public ResponseEntity<InvitationDto> sendInvitation(InvitationDto invitationDto, Integer SenderId, Integer reciverId) {
        if (invitationDto.senderId() == invitationDto.reciverId()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.areFriends(SenderId, reciverId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Invitation> invitation = invitationRepository.findBySender_IdAndReceiver_Id(invitationDto.senderId(), invitationDto.reciverId());
        if (invitation.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        System.out.println("invitationDto.senderId" + invitationDto.senderId());
        System.out.println("invitationDto.reciverId" + invitationDto.reciverId());
        Invitation invitationToSave = Invitation.builder().sender(userService.getUserById(invitationDto.senderId())).receiver(userService.getUserById(invitationDto.reciverId())).build();
        invitationRepository.save(invitationToSave);
        return ResponseEntity.ok(invitationDto);
    }

    @Transactional
    public ResponseEntity<InvitationDto> acceptInvitation(InvitationDto invitationDto) {
        return invitationRepository.findBySender_IdAndReceiver_Id(invitationDto.senderId(), invitationDto.reciverId()).map(invitation -> {
            userService.addFriend(invitationDto);
            invitationRepository.delete(invitation);
            return ResponseEntity.ok(invitationDto);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    public ResponseEntity<InvitationDto> declineInvitation(InvitationDto invitationDto) {
        return invitationRepository.findBySender_IdAndReceiver_Id(invitationDto.senderId(), invitationDto.reciverId()).map(invitation -> {
            invitationRepository.delete(invitation);
            return ResponseEntity.ok(invitationDto);
        }).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    public ResponseEntity<List<UserDto>> GetInvitations(Integer userId) {
        var user = userService.getUserById(userId);
        var invitations = invitationRepository.findByReceiver(user);
        if (invitations.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        var list = new ArrayList<UserDto>();
        for (var invitation : invitations) {
            UserDto userDto = new UserDto(invitation.getSender().getAuthId(), invitation.getSender().getFirstName(), invitation.getSender().getLastName());
            list.add(userDto);
        }
        return ResponseEntity.ok(list);
    }


    public boolean doesInvitationExist(Integer senderId, Integer reciverId) {
        if (senderId == null || reciverId == null) {
            return false;
        }
        var invitation = invitationRepository.findBySender_IdAndReceiver_Id(senderId, reciverId);
        var invitation2 = invitationRepository.findBySender_IdAndReceiver_Id(reciverId, senderId);
        return invitation.isEmpty() && invitation2.isEmpty();
    }


}
