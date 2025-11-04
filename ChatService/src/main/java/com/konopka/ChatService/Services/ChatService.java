package com.konopka.ChatService.Services;

import com.konopka.ChatService.Pojo.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

//TODO
//    public void saveMessage(message, senderUsername){
//
//    }
//
        public void sendPrivateMessage(ChatMessage message) {

//            final String senderUsername = principal.getName();
            final String senderUsername = message.getSender();
            final String recipientUsername = message.getRecipient();

            if (recipientUsername == null || recipientUsername.isEmpty()) {
                return;
            }

    //        MessageEntity savedMessage = chatService.saveMessage(message, senderUsername);
            System.out.println(message);
            messagingTemplate.convertAndSendToUser(
                    recipientUsername,
                    "/queue/messages",
                    message.getContent()
            );

             messagingTemplate.convertAndSendToUser(
                    senderUsername,
                    "/queue/messages",
                    message.getContent()
            );
            System.out.println(senderUsername + "    " + recipientUsername);

        }



}
