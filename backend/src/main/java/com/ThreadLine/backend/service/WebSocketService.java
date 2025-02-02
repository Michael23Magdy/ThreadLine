package com.ThreadLine.backend.service;

import com.ThreadLine.backend.controller.WebSocketController;
import com.ThreadLine.backend.dto.events.SimulationEvent;
import com.ThreadLine.backend.exception.wrapper.JsonProcessingExceptionWrapper;
import com.ThreadLine.backend.observer.WebSocketSubscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class WebSocketService implements WebSocketSubscriber {
    private final WebSocketController simulationController;
    private final ObjectMapper objectMapper;
    private final BlockingQueue<SimulationEvent> messageQueue = new LinkedBlockingQueue<>();

    public WebSocketService(@Lazy WebSocketController simulationController, ObjectMapper objectMapper) {
        this.simulationController = simulationController;
        this.objectMapper = objectMapper;
    }

    public synchronized void notify(SimulationEvent simulationEvent) {
        try {
            System.out.println("Sending update: " + objectMapper.writeValueAsString(simulationEvent));
            messageQueue.add(simulationEvent);
        } catch (JsonProcessingException e) {
            throw new JsonProcessingExceptionWrapper("Failed to serialize simulation event", e);
        }
    }

    @PostConstruct
    public void init() {
        new Thread(() -> {
            while (true) {
                try {
                    SimulationEvent update = messageQueue.take();
                    simulationController.sendProductionUpdate(objectMapper.writeValueAsString(update));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (JsonProcessingException e) {
                    throw new JsonProcessingExceptionWrapper("Failed to serialize simulation event", e);
                }
            }
        }).start();
    }
}
