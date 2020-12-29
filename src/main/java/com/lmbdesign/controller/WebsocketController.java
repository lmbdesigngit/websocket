package com.lmbdesign.controller;

import com.lmbdesign.controller.model.Greeting;
import com.lmbdesign.controller.model.HelloMessage;
import com.lmbdesign.service.GreetingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

@Slf4j
@Controller
public class WebsocketController {

    private final GreetingService greetingService;

    WebsocketController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    //@MessageMapping("/hello")
    //@SendTo("/topic/greetings")
    @MessageMapping("/hello")
    @SendToUser("/topic/greetings")
    public Greeting greeting(HelloMessage message, @Header("simpSessionId") String sessionId, Principal principal) throws Exception {
        log.info("Received greeting message {} from {} with sessionId {}", message, principal.getName(), sessionId);
        greetingService.addUserName(principal.getName());
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
//    public Greeting greeting(HelloMessage message) throws Exception {
//        //Thread.sleep(1000); // simulated delay
//        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
//    }

        //public Greeting greeting(HelloMessage message) throws Exception {
        //Thread.sleep(1000); // simulated delay
       // return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    //}
}
