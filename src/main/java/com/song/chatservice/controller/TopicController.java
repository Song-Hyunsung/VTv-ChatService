package com.song.chatservice.controller;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import com.song.chatservice.repository.ChatRepository;
import com.song.chatservice.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic-api")
public class TopicController {
    @Autowired
    TopicRepository repository;

    @Autowired
    ChatRepository chatRepository;

    @GetMapping("/")
    public List<Topic> getAllTopic(){
        return this.repository.findAll();
    }

    @PostMapping("/")
    public Topic addTopic(@RequestBody Topic topic){
        return this.repository.save(topic);
    }

    @PostMapping("/{topicName}")
    public void addChatMessage(@PathVariable String topicName,
                               @RequestBody ChatMessage message){
        this.chatRepository.addChatMessage(topicName, message);
    }
}
