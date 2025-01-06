package com.ThreadLine.backend.dto.events;

public class MachineUpdate extends SimulationEvent {
    private boolean active;
    private String color;

    public MachineUpdate(String id, boolean active, String color) {
        setType("machine");
        setId(id);
        this.active = active;
        this.color = color;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}