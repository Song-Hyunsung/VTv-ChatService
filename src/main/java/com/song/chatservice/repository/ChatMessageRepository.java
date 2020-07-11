package com.song.chatservice.repository;

import com.song.chatservice.collection.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    public List<ChatMessage> findAll();
    public List<ChatMessage> findByTopicName(String topicName);
    public ChatMessage save(ChatMessage message);
}
