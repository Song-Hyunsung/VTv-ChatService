package com.song.chatservice.repository;

import com.song.chatservice.collection.ChatMessage;

public interface ChatRepository {
    public void addChatMessage(String topicName, ChatMessage message);
}
