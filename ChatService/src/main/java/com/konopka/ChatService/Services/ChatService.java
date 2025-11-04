package com.konopka.ChatService.Services;

import com.konopka.ChatService.Entites.Message;
import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Repositories.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;
    private final MessageService messageService;

    public ChatService(SimpMessagingTemplate messagingTemplate, MessageRepository messageRepository, MessageService messageService) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }


    public ChatMessage sendPrivateMessage(ChatMessage chatMessage,  Principal principal) {
        final String senderUsername = principal.toString();
        final String recipientUsername = chatMessage.getRecipient();
        if (recipientUsername == null || recipientUsername.isEmpty()) {
            throw new RuntimeException("recipientUsername is null");
        }
        try{
            messagingTemplate.convertAndSendToUser(recipientUsername,"/private", chatMessage);
            messagingTemplate.convertAndSendToUser(senderUsername,"/private", chatMessage);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        messageService.save(chatMessage, senderUsername);
        return chatMessage;
    }


}
