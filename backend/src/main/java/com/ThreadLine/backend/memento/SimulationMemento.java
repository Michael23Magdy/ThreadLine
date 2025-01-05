package com.ThreadLine.backend.memento;

import com.ThreadLine.backend.model.Machine;
import com.ThreadLine.backend.model.Queue;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//* SimulationRepository is the originator
public record SimulationMemento(Map<String, Machine> machines, Map<String, Queue> queues, int products) {
    public SimulationMemento(Map<String, Machine> machines, Map<String, Queue> queues, int products) {
        this.machines = machines
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().clone()
                ));

        this.queues = queues
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().clone()
                ));
        this.products = products;
    }
}
