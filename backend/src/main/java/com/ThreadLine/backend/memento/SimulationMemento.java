package com.ThreadLine.backend.memento;

import com.ThreadLine.backend.model.Machine;
import com.ThreadLine.backend.model.Queue;

import java.util.HashMap;
import java.util.Map;

//* SimulationRepository is the originator
public record SimulationMemento(Map<String, Machine> machines, Map<String, Queue> queues, int products) {
    public SimulationMemento(Map<String, Machine> machines, Map<String, Queue> queues, int products) {
        this.machines = new HashMap<>(machines);
        this.queues = new HashMap<>(queues);
        this.products = products;
    }
}
