package com.ThreadLine.backend.controller;

import com.ThreadLine.backend.service.SimulationInitializer;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SimulationController {
    private final SimpMessagingTemplate messagingTemplate;
    private final SimulationInitializer initializer;

    public SimulationController(SimpMessagingTemplate messagingTemplate, SimulationInitializer initializer) {
        this.messagingTemplate = messagingTemplate;
        this.initializer = initializer;
    }

    public void sendProductionUpdate(String message) {
        messagingTemplate.convertAndSend("/topic/simulation", message);
    }

    @MessageMapping("/subscribe")
    @SendTo("/topic/simulation")
    public String handleSubscription(String message) {
        initializer.start();
        return "Subscribed Successfully";
    }
}