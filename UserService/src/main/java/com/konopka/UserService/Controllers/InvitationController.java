package com.konopka.UserService.Controllers;

import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Repositories.InvitationRepostitory;
import com.konopka.UserService.Services.InvitationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/invitation")
public class InvitationController {


    private final InvitationService invitationService;


    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }


    @PostMapping("/send")
    public ResponseEntity<InvitationDto> sendInvitation(@RequestBody InvitationDto invitationDto){
        return invitationService.sendInvitation(invitationDto);
    }


    @PostMapping("/accept")
    public ResponseEntity<InvitationDto> acceptInvitation(@RequestBody InvitationDto invitationDto){
        return invitationService.acceptInvitation(invitationDto);
    }




}
