package com.konopka.ChatService.Services;

import com.konopka.ChatService.Entites.Message;
import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> findAllMessagesForConversationId(Long conversationId) {
        return messageRepository.findAllByConversationId(conversationId);
    }

    public Message save(ChatMessage chatMessage, String senderUsername) {
        Message message = Message.builder()
                .content(chatMessage.getContent())
                .recipient(chatMessage.getRecipient())
                .ConversationId(chatMessage.getConversationId())
                .sender(senderUsername)
                .build();

        return messageRepository.save(message);
    }


}
