package com.konopka.ChatService.Controllers;


import com.konopka.ChatService.Dto.UserAuthIdChatHistory;
import com.konopka.ChatService.Entites.Message;
import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Services.MessageService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/conversation/{userAuthId}")
    public List<Message> getConversation(@PathVariable String userAuthId, Principal principal) {
        return messageService.findAllMessagesForConversationId(userAuthId, principal.getName());
    }


}
