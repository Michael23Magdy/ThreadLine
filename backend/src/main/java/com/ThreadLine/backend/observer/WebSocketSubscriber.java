package com.ThreadLine.backend.observer;

import com.ThreadLine.backend.dto.SimulationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface WebSocketSubscriber {
    void notify(SimulationEvent event) throws JsonProcessingException;
}