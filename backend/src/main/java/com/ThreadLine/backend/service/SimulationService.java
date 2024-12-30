package com.ThreadLine.backend.service;

import com.ThreadLine.backend.controller.SimulationController;
import com.ThreadLine.backend.dto.SimulationEvent;
import com.ThreadLine.backend.observer.Subscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class SimulationService implements Subscriber {
    private final SimulationController simulationController;
    private final ObjectMapper objectMapper;

    public SimulationService(@Lazy SimulationController simulationController, ObjectMapper objectMapper) {
        this.simulationController = simulationController;
        this.objectMapper = objectMapper;
    }

    public void notify(SimulationEvent simulationEvent) throws JsonProcessingException {
        System.out.println("Sending update: " + objectMapper.writeValueAsString(simulationEvent));
        simulationController.sendProductionUpdate(objectMapper.writeValueAsString(simulationEvent));
    }
}
