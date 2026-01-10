package com.konopka.ChatService.Pojo;

import com.konopka.ChatService.Entites.MessageType;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class ChatMessage {

    private MessageType type;
    private String content;
    private String sender;
    private String recipient;
    private String ConversationId;

}
