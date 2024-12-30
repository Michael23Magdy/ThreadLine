package com.ThreadLine.backend.dto;

import lombok.Data;

@Data
public abstract class SimulationEvent {
    private String type; //* "machine" or "queue"
    private String id;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}