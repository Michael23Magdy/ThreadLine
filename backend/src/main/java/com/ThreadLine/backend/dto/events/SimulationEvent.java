package com.ThreadLine.backend.dto.events;

import lombok.Data;

@Data
public abstract class SimulationEvent {
    private String type; //* "machine" or "queue"
    private String id;
}