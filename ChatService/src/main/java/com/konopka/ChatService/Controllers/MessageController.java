package com.konopka.ChatService.Controllers;


import com.konopka.ChatService.Entites.Message;
import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Services.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/conversation/{conversationId}")
    public List<Message> getConversation(@PathVariable("conversationId") Long conversationId){
        return messageService.findAllMessagesForConversationId(conversationId);
    }


}
