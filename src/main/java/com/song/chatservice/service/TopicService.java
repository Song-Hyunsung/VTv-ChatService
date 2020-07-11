package com.song.chatservice.service;

import com.song.chatservice.collection.Topic;
import com.song.chatservice.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {
    private TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository){
        this.topicRepository = topicRepository;
    }

    public List<Topic> getAllTopic(){
        return this.topicRepository.findAll();
    }

    public Optional<Topic> findByTopicName(String topicName){
        return this.topicRepository.findByTopicName(topicName);
    }

    public Topic addNewTopic(Topic topic){
        return this.topicRepository.save(topic);
    }
}
