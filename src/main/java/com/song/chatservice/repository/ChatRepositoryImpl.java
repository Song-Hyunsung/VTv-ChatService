package com.song.chatservice.repository;

import com.mongodb.client.result.UpdateResult;
import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
@Document(collection = "topic")
public class ChatRepositoryImpl implements ChatRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void addChatMessage(String topicName, ChatMessage message) {
        Query query = new Query();
        query.addCriteria(Criteria.where("topicName").is(topicName));

        Update update = new Update();
        update.push("chatMessages", message);

        mongoTemplate.updateFirst(query, update, Topic.class);
    }
}
