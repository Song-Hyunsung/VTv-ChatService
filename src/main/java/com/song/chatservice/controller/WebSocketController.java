package com.song.chatservice.controller;

import com.song.chatservice.collection.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    // From Websocket Configuration, we stated that clients with destination "/app" is routed message-handling methods
    // Message with destination /app/chat.sendMessage will be routed here
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        if(chatMessage.getType() == ChatMessage.MessageType.CHAT){
            return chatMessage;
        } else {
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setContent("WRONG MESSAGE TYPE");
            return errorMessage;
        }
    }

    // Message with destination /app/chat.addUser will be routed here
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}