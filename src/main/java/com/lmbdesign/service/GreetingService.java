package com.lmbdesign.service;

import com.lmbdesign.controller.model.Greeting;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.*;

@Data
@Slf4j
@Service
public class GreetingService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final String SIMP_SESSION_ID = "simpSessionId";
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/greetings";
    private List<String> userNames = new ArrayList<>();

    /**
     * Handles WebSocket connection events
     */
    @EventListener(SessionConnectEvent.class)
    public void handleWebsocketConnectListener(SessionConnectEvent event) {
        log.info(String.format("WebSocket connection established for sessionID %s",
                getSessionIdFromMessageHeaders(event)));
    }

    /**
     * Handles WebSocket disconnection events
     */
    @EventListener(SessionDisconnectEvent.class)
    public void handleWebsocketDisconnectListener(SessionDisconnectEvent event) {
        log.info(String.format("WebSocket connection closed for sessionID %s",
                getSessionIdFromMessageHeaders(event)));
    }

    GreetingService(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    public void sendMessages() {
        for (String userName : userNames) {
            simpMessagingTemplate.convertAndSendToUser(userName, WS_MESSAGE_TRANSFER_DESTINATION,
                    new Greeting("Hallo " + userName + " at " + new Date().toString()));
        }
        simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION, new Greeting("OK THEN"));
    }

    public void addUserName(String username) {
        userNames.add(username);
    }

    private String getSessionIdFromMessageHeaders(SessionDisconnectEvent event) {
        Map<String, Object> headers = event.getMessage().getHeaders();
        return Objects.requireNonNull(headers.get(SIMP_SESSION_ID)).toString();
    }

    private String getSessionIdFromMessageHeaders(SessionConnectEvent event) {
        Map<String, Object> headers = event.getMessage().getHeaders();
        return Objects.requireNonNull(headers.get(SIMP_SESSION_ID)).toString();
    }

}
