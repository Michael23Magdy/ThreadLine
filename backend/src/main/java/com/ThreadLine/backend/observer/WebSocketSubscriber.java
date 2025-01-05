package com.ThreadLine.backend.observer;

import com.ThreadLine.backend.dto.SimulationEvent;

public interface WebSocketSubscriber {
    void notify(SimulationEvent event);
}