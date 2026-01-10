package com.konopka.UserService.Controllers;

import com.konopka.UserService.Dto.InvitationDto;
import com.konopka.UserService.Dto.InvitationRequest;
import com.konopka.UserService.Dto.UserDto;
import com.konopka.UserService.Services.InvitationService;
import com.konopka.UserService.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user/invitation")
public class InvitationController {


    private final InvitationService invitationService;
    private final UserService userService;


    public InvitationController(InvitationService invitationService, UserService userService) {
        this.invitationService = invitationService;
        this.userService = userService;
    }


    @PostMapping("/send")
    public ResponseEntity<InvitationDto> sendInvitation(@RequestBody InvitationRequest request, Principal principal) {
        InvitationDto invitationDto = new InvitationDto(userService.getUserIdByAuthId(Integer.parseInt(principal.getName())), userService.getUserIdByAuthId(request.reciverId()));
        return invitationService.sendInvitation(invitationDto, Integer.valueOf(principal.getName()), request.reciverId());
    }

    @PostMapping("/accept")
    public ResponseEntity<InvitationDto> acceptInvitation(@RequestBody InvitationRequest invitationRequest, Principal principal) {
        InvitationDto invitationDto = new InvitationDto(userService.getUserIdByAuthId(invitationRequest.reciverId()), userService.getUserIdByAuthId(Integer.parseInt(principal.getName())));
        return invitationService.acceptInvitation(invitationDto);
    }

    @PostMapping("/decline")
    public ResponseEntity<InvitationDto> declineInvitation(@RequestBody InvitationRequest invitationRequest, Principal principal) {
        InvitationDto invitationDto = new InvitationDto(userService.getUserIdByAuthId(invitationRequest.reciverId()), userService.getUserIdByAuthId(Integer.parseInt(principal.getName())));
        return invitationService.declineInvitation(invitationDto);
    }

    @GetMapping("/invitations")
    public List<UserDto> getInvitations(Principal principal){
        var userId = userService.getUserIdByAuthId(Integer.parseInt(principal.getName()));
        return invitationService.GetInvitations(userId).getBody();
    }


}
