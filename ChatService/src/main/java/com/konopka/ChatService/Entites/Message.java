package com.konopka.ChatService.Entites;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode
@Document("Message")
public class Message {

    @Id
    private String id;
    private String content;
    private String sender;
    private String recipient;
    private String conversationId;

}
