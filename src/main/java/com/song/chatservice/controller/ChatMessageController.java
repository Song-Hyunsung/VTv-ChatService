package com.song.chatservice.controller;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("chat-api")
public class ChatMessageController {
    @Autowired
    private ChatMessageService chatMessageService;

    @GetMapping("/")
    public List<ChatMessage> getAllChatMessages(){
        return this.chatMessageService.getAllChatMessages();
    }

    @GetMapping("/{topicName}/all")
    public List<ChatMessage> getAllChatMessagesByTopicName(@PathVariable String topicName){
        return this.chatMessageService.getAllChatMessagesByTopicName(topicName);
    }

    @PostMapping("/addChat")
    public ChatMessage addChatMessage(@RequestBody ChatMessage message){
        return this.chatMessageService.addChatMessage(message);
    }
}
