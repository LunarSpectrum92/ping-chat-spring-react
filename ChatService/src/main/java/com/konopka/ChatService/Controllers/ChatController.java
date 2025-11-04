package com.konopka.ChatService.Controllers;


import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;


    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }
//
//,
//    Principal user,
//    @Header("simpSessionId") String sessionId
    @MessageMapping("/private-message")
    public ChatMessage handlePrivateMessage(  @Payload ChatMessage message
    ) {

        messagingTemplate.convertAndSendToUser(message.getRecipient(),"/private", message);
        System.out.println(message);
        return message;
    }


//    @GetMapping("/test")
//    public static String test(){
//        return "test";
//    }


}
