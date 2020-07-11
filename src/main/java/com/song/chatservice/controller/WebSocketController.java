package com.song.chatservice.controller;

import com.song.chatservice.collection.ChatMessage;
import com.song.chatservice.collection.Topic;
import com.song.chatservice.service.ChatMessageService;
import com.song.chatservice.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketController {
    @Autowired
    private TopicService topicService;
    private ChatMessageService chatMessageService;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    // From Websocket Configuration, we stated that clients with destination "/app" is routed message-handling methods
    // Message with destination /app/chat.sendMessage will be routed here
    @MessageMapping("/{topicName}/chat.sendMessage")
    @SendTo("/topic/{topicName}")
    public ChatMessage sendMessage(@DestinationVariable String topicName,
                                   @Payload ChatMessage chatMessage){
        if(chatMessage.getType() == ChatMessage.MessageType.CHAT){
            this.chatMessageService.addChatMessage(chatMessage);
            return chatMessage;
        } else {
            ChatMessage errorMessage = new ChatMessage();
            errorMessage.setContent("WRONG MESSAGE TYPE");
            return errorMessage;
        }
    }

    // Message with destination /app/chat.addUser will be routed here
    @MessageMapping("/{topicName}/chat.addUser")
    @SendTo("/topic/{topicName}")
    public ChatMessage addUser(@DestinationVariable String topicName,
                               @Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor){
        logger.info("received topic name from path variable in addUser: " + topicName);
        boolean topicExists = this.topicService.findByTopicName(topicName).isPresent();

        if(!topicExists){
            Topic newTopic = new Topic();
            newTopic.setTopicName(topicName);
            this.topicService.addNewTopic(newTopic);
        }

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        this.chatMessageService.addChatMessage(chatMessage);
        return chatMessage;
    }
}