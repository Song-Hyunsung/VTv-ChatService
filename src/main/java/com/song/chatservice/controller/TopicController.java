package com.song.chatservice.controller;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import com.song.chatservice.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic-api")
public class TopicController {
    @Autowired
    private TopicService service;

    @GetMapping("/")
    public List<Topic> getAllTopic(){
        return this.service.getAllTopic();
    }

    @PostMapping("/")
    public Topic addTopic(@RequestBody Topic topic){
        return this.service.addNewTopic(topic);
    }

    @PostMapping("/{topicName}")
    public void addChatMessage(@PathVariable String topicName,
                               @RequestBody ChatMessage message){
        this.service.addChatMessage(topicName, message);
    }
}
