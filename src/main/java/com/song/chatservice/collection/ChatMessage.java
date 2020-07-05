package com.song.chatservice.collection;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class ChatMessage {
    @Id
    private ObjectId id;
    private MessageType type;
    private String content;
    private String sender;

    // Enumerations serve the purpose of representing group of named constants, at compile time
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
