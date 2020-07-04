package com.song.chatservice.repository;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends MongoRepository<Topic, String> {
    public List<Topic> findAll();
    public Optional<Topic> findById(String id);
    public Optional<Topic> findByTopicName(String topicName);
    public Topic save(Topic topic);
}
