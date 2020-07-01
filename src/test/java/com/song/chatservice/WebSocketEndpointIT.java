package com.song.chatservice;

import com.song.chatservice.model.ChatMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketEndpointIT {
    //  set the random port from the webEnvironment
    @Value("${local.server.port}")
    private int port;

    //  set URL variables
    private String URL;
    private static final String ADD_USER = "/app/chat.addUser";
    private static final String SEND_MESSAGE = "/app/chat.sendMessage";
    private static final String SUBSCRIBE = "/topic/public";

    private CompletableFuture<ChatMessage> completableFuture;

    @Before
    public void setup() {
        completableFuture = new CompletableFuture<>();
        URL = "http://localhost:" + port + "/ws";
    }

    @Test
    public void testAddUserEndpoint() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = initializeStompClient();
        StompSession stompSession = initializeStompSession(stompClient);

//      Create a test "JOIN" ChatMessage
        ChatMessage addUserTestChatMessage = new ChatMessage();
        addUserTestChatMessage.setType(ChatMessage.MessageType.JOIN);
        addUserTestChatMessage.setSender("testAddUserEndpointSender");
        addUserTestChatMessage.setContent("");

//      Send above test "JOIN" ChatMessage to the stomp session
        stompSession.send(ADD_USER, addUserTestChatMessage);

//      After 10 second, the future will timeout if response is not received and retrieve the sent "JOIN" ChatMessage
        ChatMessage sentAddUserTestChatMessage = completableFuture.get(10, SECONDS);

//      Test that created test "JOIN" ChatMessage is not the same reference as the received "JOIN" ChatMessage
        assertThat(addUserTestChatMessage).isNotSameAs(sentAddUserTestChatMessage);
//      Test that created test "JOIN" ChatMessage is equal, field by field, as the received "JOIN" ChatMessage
        assertThat(addUserTestChatMessage).isEqualToComparingFieldByField(sentAddUserTestChatMessage);

        stompSession.disconnect();
        stompClient.stop();
    }

    @Test
    public void testSendMessageEndpoint() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = initializeStompClient();
        StompSession stompSession = initializeStompSession(stompClient);

        ChatMessage sendMessageTestChatMessage = new ChatMessage();
        sendMessageTestChatMessage.setType(ChatMessage.MessageType.CHAT);
        sendMessageTestChatMessage.setContent("Test Content");

        stompSession.send(SEND_MESSAGE, sendMessageTestChatMessage);

        ChatMessage sentSendMessageTest = completableFuture.get(10, SECONDS);

        assertThat(sendMessageTestChatMessage).isNotSameAs(sentSendMessageTest);
        assertThat(sendMessageTestChatMessage).isEqualToComparingFieldByField(sentSendMessageTest);

        stompSession.disconnect();
        stompClient.stop();
    }

    @Test
    public void testErrorSendMessageEndpoint() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketStompClient stompClient = initializeStompClient();
        StompSession stompSession = initializeStompSession(stompClient);

        ChatMessage sendMessageErrorTestChatMessage = new ChatMessage();
        sendMessageErrorTestChatMessage.setType(ChatMessage.MessageType.LEAVE);
        sendMessageErrorTestChatMessage.setContent("Error Test Content");

        stompSession.send(SEND_MESSAGE, sendMessageErrorTestChatMessage);

        ChatMessage sentSendMessageErrorTest = completableFuture.get(10, SECONDS);

        assertThat(sentSendMessageErrorTest.getContent()).isEqualTo("WRONG MESSAGE TYPE");

        stompSession.disconnect();
        stompClient.stop();
    }

    private StompSession initializeStompSession(WebSocketStompClient stompClient) throws ExecutionException, InterruptedException {
//      Create a stomp session by connecting the stomp client to the websocket URL and providing  session handler for logging
        StompSession stompSession = stompClient.connect(URL, new CustomStompSessionHandler()).get();

//      Subscribe to "/topic/public", the websocket channel with custom handler
//      so that incoming payload can be expected and placed into completableFuture
        stompSession.subscribe(SUBSCRIBE, new CustomStompFrameHandler());

        return stompSession;
    }

    private WebSocketStompClient initializeStompClient(){
//      Initialize a websocket stomp client, providing a list of transport to SockJS client, into websocket stomp client
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));

//      Set the stomp client to use Jackson Message Converter, if it uses the default String Message Converter
//      it causes some class casting error when receiving the payload (ChatMessage object).
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        return stompClient;
    }

//  Create a method that returns a list of transport methods which will be used as the transport protocol
//  This one uses only standard websocket client, there could be multiple transports in case the client browser
//  doesn't support websocket transport. In that case, it will mimic websocket behavior with other protocol.
    private List<Transport> createTransportClient(){
        List<Transport> fallbackTransportList = new ArrayList<>(1);
        fallbackTransportList.add((Transport) new WebSocketTransport(new StandardWebSocketClient()));
        return fallbackTransportList;
    }

//  Method that provides logging after a stomp client has connected and exception was handled
    private class CustomStompSessionHandler extends StompSessionHandlerAdapter {
        private final Logger logger = LoggerFactory.getLogger(CustomStompSessionHandler.class);

        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders){
            logger.info("WebSocketEndpointIT: New Session Established: " + session.getSessionId());
        }

        @Override
        public void handleException(StompSession session,
                                    StompCommand command,
                                    StompHeaders headers,
                                    byte[] payload,
                                    Throwable exception){
            logger.error("WebSocketEndpointIT: Exception thrown: " + exception);
        }
    }

//  Method that handles incoming stomp frames. getPayloadType will return ChatMessage.class, which makes the
//  client to anticipate ChatMessage class as the incoming payload.
//  The handleFrame method will cast the payload into ChatMessage and put it into completableFuture to be tested.
    private class CustomStompFrameHandler implements StompFrameHandler {
        private final Logger logger = LoggerFactory.getLogger(CustomStompSessionHandler.class);

        @Override
        public Type getPayloadType(StompHeaders stompHeaders){
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object payload){
            ChatMessage message = (ChatMessage) payload;
            completableFuture.isCancelled();
            logger.info("WebSocketEndpointIT\nReceived content: " + message.getContent() +
                        "\nFrom: " + message.getSender() +
                        "\nWith Type: " + message.getType());
            completableFuture.complete(message);
        }
    }
}
