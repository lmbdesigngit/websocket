package com.lmbdesign.service;

import com.lmbdesign.controller.model.Greeting;
import com.lmbdesign.controller.model.HelloMessage;
import com.lmbdesign.controller.rest.model.Player;
import com.lmbdesign.controller.rest.model.Room;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.*;

@Data
@Slf4j
@Service
public class GreetingService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final String SIMP_SESSION_ID = "simpSessionId";
    private static final String WS_MESSAGE_TRANSFER_DESTINATION = "/topic/greetings";
    private List<String> userNames = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    @Autowired
    private RoomService roomService;


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
//        for (String userName : userNames) {
//            simpMessagingTemplate.convertAndSendToUser(userName, WS_MESSAGE_TRANSFER_DESTINATION,
//                    new Greeting("Hallo " + userName + " at " + new Date().toString()));
//        }



        for (Player player : players) {
            simpMessagingTemplate.convertAndSendToUser(player.getPrincipalName(), WS_MESSAGE_TRANSFER_DESTINATION,
                    new Greeting("Hey " + player.getName() + " " + players + " at " + new Date().toString()));
        }

        //simpMessagingTemplate.convertAndSend(WS_MESSAGE_TRANSFER_DESTINATION, new Greeting("OK THEN"));
    }

    public void addUserName(String username) {
        if(userNames.contains(username)) {
            return;
        }
        userNames.add(username);
    }

    public void addPlayer(HelloMessage message, Principal principal) {
        if(userNames.contains(principal.getName())) {
            return;
        }
        userNames.add(principal.getName());

        Player player = new Player(message.getName());
        player.setRoom(roomService.getRoom());
        player.setPrincipalName(principal.getName());

        players.add(player);
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
