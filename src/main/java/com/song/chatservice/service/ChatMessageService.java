package com.song.chatservice.service;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMessageService {
    private ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository){
        this.chatMessageRepository = chatMessageRepository;
    }

    public List<ChatMessage> getAllChatMessages(){
        return this.chatMessageRepository.findAll();
    }

    public List<ChatMessage> getAllChatMessagesByTopicName(String topicName){
        return this.chatMessageRepository.findByTopicName(topicName);
    }

    public ChatMessage addChatMessage(ChatMessage chatMessage){
        return this.chatMessageRepository.save(chatMessage);
    }
}
