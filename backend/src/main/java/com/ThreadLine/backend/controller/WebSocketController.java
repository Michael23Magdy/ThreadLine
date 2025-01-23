package com.ThreadLine.backend.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public synchronized void sendProductionUpdate(String message) {
        messagingTemplate.convertAndSend("/topic/simulation", message);
    }

    @MessageMapping("/subscribe")
    @SendTo("/topic/simulation")
    public String handleSubscription(String message) {
        return "Subscribed Successfully";
    }
}