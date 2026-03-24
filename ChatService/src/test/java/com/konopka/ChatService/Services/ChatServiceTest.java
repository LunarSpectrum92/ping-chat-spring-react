package com.konopka.ChatService.Services;

import com.konopka.ChatService.Pojo.ChatMessage;
import com.konopka.ChatService.Repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.Principal;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    SimpMessagingTemplate messagingTemplate;
    @Mock
    MessageRepository messageRepository;
    @Mock
    MessageService messageService;

    @InjectMocks
    ChatService chatService;




    @Test
    void sendPrivateMessage_recipientUsernameNull_throwRuntimeException() {
        Principal principal = mock(Principal.class);
        ChatMessage chatMessage = ChatMessage.builder().recipient("2").build();
        when(principal.getName()).thenReturn(null);

        assertThatThrownBy(() -> chatService.sendPrivateMessage(chatMessage, principal)).isInstanceOf(RuntimeException.class);
        verify(messagingTemplate, never()).convertAndSendToUser(anyString(),anyString(),any());
    }

    @Test
    void sendPrivateMessage_shouldSendToBothUsersWithCorrectPath() {
        Principal principal = mock(Principal.class);
        ChatMessage chatMessage = ChatMessage.builder().recipient("2").content("Hello").build();
        when(principal.getName()).thenReturn("1");
        when(messageService.generateConversationId("1", "2")).thenReturn("1_2");
        ArgumentCaptor<String> userCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> pathCaptor = ArgumentCaptor.forClass(String.class);

        ChatMessage response = chatService.sendPrivateMessage(chatMessage, principal);

        verify(messagingTemplate, times(2)).convertAndSendToUser(
                userCaptor.capture(),
                pathCaptor.capture(),
                eq(chatMessage)
        );
        assertThat(userCaptor.getAllValues()).containsExactly("2", "1");
        assertThat(pathCaptor.getAllValues()).allMatch(path -> path.equals("/queue/chat/1_2"));
        assertThat(response.getSender()).isEqualTo("1");
    }



}