package com.song.chatservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
// Annotation used to enable WebSocket Server
@EnableWebSocketMessageBroker
// This configuration uses in-memory message broker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // This method provides endpoint to client which they can connect to the websocket server
    // SockJS is used to enable fallback options for browsers that doesn't support websocket
    // STOMP comes from Spring framework's Simple Text Oriented Messaging Protocol
    // Websocket is communication protocol, STOMP provides simple messaging protocol
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/ws").withSockJS();
    }

    // This method configures message broker that will be used to route messages from one to another
    // First line defines that messages with destination "/app" should be routed to message-handling methods
    // Second line defines that messages with destination "/topic" should be routed to message broker
    // Message broker broadcasts messages to all the connected clients who are subscribed to a specific topic
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry){
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }
}
