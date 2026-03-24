package com.konopka.ChatService.Services;

import com.konopka.ChatService.Entites.Message;
import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    @Spy
    private MessageService messageService;

    @Test
    void findAllMessagesForConversationId_messagesFoundSuccessfully_returnAllMessagesForConversationId() {
        String FirstUserUsername = "1";
        String SecoundUserUsername = "2";
        String ConversationId = "1_2";
        when(messageService.generateConversationId(FirstUserUsername, SecoundUserUsername)).thenReturn(ConversationId);
        when(messageRepository.findAllByConversationId(ConversationId)).thenReturn(List.of(Message.builder().sender("1").recipient("2").conversationId(ConversationId).build()));

        List<Message> result = messageService.findAllMessagesForConversationId(FirstUserUsername, SecoundUserUsername);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getConversationId()).isEqualTo(ConversationId);
        assertThat(result.get(0).getSender()).isEqualTo("1");
        assertThat(result.get(0).getRecipient()).isEqualTo("2");
        verify(messageRepository).findAllByConversationId(ConversationId);
    }

    @Test
    void save_ValidChatMessageAndSender_ReturnsSavedMessage() {
        String senderUsername = "1";
        String conversationId = "1_2";
        ChatMessage chatMessage = ChatMessage.builder()
                .content("test")
                .recipient("user2")
                .build();

        Message savedMessage = Message.builder()
                .content("test")
                .recipient("2")
                .sender(senderUsername)
                .conversationId(conversationId)
                .build();

        when(messageRepository.save(org.mockito.ArgumentMatchers.any(Message.class)))
                .thenReturn(savedMessage);

        Message result = messageService.save(chatMessage, senderUsername, conversationId);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("test");
        assertThat(result.getSender()).isEqualTo(senderUsername);
        assertThat(result.getRecipient()).isEqualTo("2");
        assertThat(result.getConversationId()).isEqualTo(conversationId);

        verify(messageRepository).save(org.mockito.ArgumentMatchers.any(Message.class));
    }

    @Test
    void generateConversationId_DifferentInputOrder_ReturnsSameSortedId() {
        String u1 = "1";
        String u2 = "2";

        String result = messageService.generateConversationId(u1, u2);
        String reverseResult = messageService.generateConversationId(u2, u1);

        String expected = "1_2";
        assertThat(result).isEqualTo(expected);
        assertThat(reverseResult).isEqualTo(expected);
    }

    @Test
    void generateConversationId_SameUsernames_ReturnsCorrectId() {
        String user = "1";

        String result = messageService.generateConversationId(user, user);

        assertThat(result).isEqualTo("1_1");
    }
}