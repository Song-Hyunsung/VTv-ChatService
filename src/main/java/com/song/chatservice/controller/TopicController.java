package com.song.chatservice.controller;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import com.song.chatservice.service.ChatMessageService;
import com.song.chatservice.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/topic-api")
public class TopicController {
    @Autowired
    private TopicService topicService;

    @GetMapping("/")
    public List<Topic> getAllTopic(){
        return this.topicService.getAllTopic();
    }

    @PostMapping("/addTopic")
    public Topic addTopic(@RequestBody Topic topic){
        return this.topicService.addNewTopic(topic);
    }
}
