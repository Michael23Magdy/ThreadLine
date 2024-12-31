package com.ThreadLine.backend.service;

import com.ThreadLine.backend.controller.WebSocketController;
import com.ThreadLine.backend.dto.SimulationEvent;
import com.ThreadLine.backend.observer.WebSocketSubscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class WebSocketService implements WebSocketSubscriber {
    private final WebSocketController simulationController;
    private final ObjectMapper objectMapper;

    public WebSocketService(@Lazy WebSocketController simulationController, ObjectMapper objectMapper) {
        this.simulationController = simulationController;
        this.objectMapper = objectMapper;
    }

    public void notify(SimulationEvent simulationEvent) throws JsonProcessingException {
        System.out.println("Sending update: " + objectMapper.writeValueAsString(simulationEvent));
        simulationController.sendProductionUpdate(objectMapper.writeValueAsString(simulationEvent));
    }
}
