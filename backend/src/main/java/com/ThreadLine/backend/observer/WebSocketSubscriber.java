package com.ThreadLine.backend.observer;

import com.ThreadLine.backend.dto.events.SimulationEvent;

public interface WebSocketSubscriber {
    void notify(SimulationEvent event);
}