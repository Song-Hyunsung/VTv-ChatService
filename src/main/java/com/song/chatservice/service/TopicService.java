package com.song.chatservice.service;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import com.song.chatservice.repository.ChatRepository;
import com.song.chatservice.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    private ChatRepository chatRepository;
    private TopicRepository topicRepository;

    public TopicService(ChatRepository chatRepository, TopicRepository topicRepository){
        this.chatRepository = chatRepository;
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopic(){
        return this.topicRepository.findAll();
    }

    public Topic addNewTopic(Topic topic){
        return this.topicRepository.save(topic);
    }

    public void addChatMessage(String topicName, ChatMessage message){
        this.chatRepository.addChatMessage(topicName, message);
    }
}
