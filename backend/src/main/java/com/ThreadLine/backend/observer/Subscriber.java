package com.ThreadLine.backend.observer;

import com.ThreadLine.backend.dto.SimulationEvent;
import com.ThreadLine.backend.model.Queue;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface Subscriber {
    void notify(SimulationEvent event) throws JsonProcessingException;
}