package com.konopka.ChatService.Repositories;


import com.konopka.ChatService.Entites.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> findAllByConversationId(Long conversationId);

}
