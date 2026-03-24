package com.konopka.ChatService.Services;

import com.konopka.ChatService.Entites.Message;
import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public List<Message> findAllMessagesForConversationId(String firstUserUsername, String secoundUserUsername) {
        final String conversationId = generateConversationId(firstUserUsername, secoundUserUsername);
        return messageRepository.findAllByConversationId(conversationId);
    }

    public Message save(ChatMessage chatMessage, String senderUsername, String conversatinId) {
        Message message = Message.builder()
                .content(chatMessage.getContent())
                .recipient(chatMessage.getRecipient())
                .conversationId(conversatinId)
                .sender(senderUsername)
                .build();

        return messageRepository.save(message);
    }

    public String generateConversationId(String u1, String u2) {
        List<String> ids = Arrays.asList(u1, u2);
        Collections.sort(ids);
        return ids.get(0) + "_" + ids.get(1);
    }

}
