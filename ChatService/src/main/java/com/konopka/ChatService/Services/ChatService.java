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


    public ChatMessage sendPrivateMessage(ChatMessage chatMessage, Principal principal) {
        final String senderUsername = principal.getName();
        final String recipientUsername = chatMessage.getRecipient();
        if (recipientUsername == null || recipientUsername.isEmpty()) {
            throw new RuntimeException("recipientUsername is null");
        }
        if(senderUsername == null || senderUsername.isEmpty()){
            throw new RuntimeException("senderUsername is null");
        }
        final String conversationId =
                messageService.generateConversationId(senderUsername, recipientUsername);

        chatMessage.setSender(senderUsername);
        chatMessage.setConversationId(conversationId);

        try {
            messagingTemplate.convertAndSendToUser(
                    recipientUsername,
                    "/queue/chat/" + conversationId,
                    chatMessage
            );

            messagingTemplate.convertAndSendToUser(
                    senderUsername,
                    "/queue/chat/" + conversationId,
                    chatMessage
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        messageService.save(chatMessage, senderUsername, conversationId);
        return chatMessage;
    }


}
