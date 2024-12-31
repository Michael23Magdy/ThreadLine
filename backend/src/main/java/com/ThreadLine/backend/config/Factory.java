package com.ThreadLine.backend.config;


import com.ThreadLine.backend.model.Machine;
import com.ThreadLine.backend.model.Queue;
import com.ThreadLine.backend.service.WebSocketService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Factory {
    private final WebSocketService simulationService;

    public Factory(WebSocketService simulationService) {
        this.simulationService = simulationService;
    }

    public Machine createMachine(String id) {
        return new Machine(id, simulationService);
    }

    public Queue createQueue(String id) {
        return new Queue(id, simulationService);
    }
}