package com.ThreadLine.backend.dto;

public class QueueUpdate extends SimulationEvent {
    private int count;

    public QueueUpdate(String id, int count) {
        setType("queue");
        setId(id);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}