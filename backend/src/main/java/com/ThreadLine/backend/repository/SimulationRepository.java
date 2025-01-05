package com.ThreadLine.backend.repository;

import com.ThreadLine.backend.config.Factory;
import com.ThreadLine.backend.memento.SimulationCaretaker;
import com.ThreadLine.backend.memento.SimulationMemento;
import com.ThreadLine.backend.model.Machine;
import com.ThreadLine.backend.model.Product;
import com.ThreadLine.backend.model.Queue;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Repository
public class SimulationRepository {
    private final Factory factory;
    private final Map<String, Machine> machines = new HashMap<>();
    private final Map<String, Queue> queues = new HashMap<>();
    private volatile Boolean isRunning = true;
    private int products = 0;
    private final SimulationCaretaker caretaker = new SimulationCaretaker();
    private Thread productGeneratorThread;
    List<Edge> edges;

    public SimulationRepository(Factory factory) {
        this.factory = factory;
    }

    @Data
    public static class SimulationConfig {
        private List<String> machines;
        private List<String> queues;
        private List<Edge> edges;
        private int products;
    }

    @Data
    public static class Edge {
        private String source;
        private String target;
    }

    public void pause() {
        isRunning = false;
        caretaker.savePauseMemento(saveState());
        stopAllMachines();
    }

    public void resume() {
        isRunning = true;
        SimulationMemento memento = caretaker.getPauseMemento();
        if (memento != null) {
            restoreState(memento, null);
            start();
        }
    }

    public void replay(int initialProducts) {
        stopAllMachines();
        isRunning = true;
        SimulationMemento memento = caretaker.getStartMemento();
        if (memento != null) {
            restoreState(memento, null);
            start();
        }
    }

    public SimulationMemento saveState() {
        return new SimulationMemento(new HashMap<>(machines), new HashMap<>(queues), products);
    }

    public void restoreState(SimulationMemento memento, Integer modifiedProductsCount) {
        clearSimulation();
        machines.putAll(memento.machines().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone()
                )));
        queues.putAll(memento.queues().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone()
                )));
        createConnections();
        this.products = Objects.requireNonNullElseGet(modifiedProductsCount, memento::products);
    }

    public SimulationRepository initialize(SimulationConfig config) {
        clearSimulation();
        createMachines(config.getMachines());
        createQueues(config.getQueues());
        this.edges = config.getEdges();
        createConnections();
        this.products = config.getProducts();
        caretaker.saveStartMemento(saveState());
        return this;
    }

    private void createMachines(List<String> machineIds) {
        machineIds.forEach(id -> machines.put(id, factory.createMachine(id)));
    }

    private void createQueues(List<String> queueIds) {
        queueIds.forEach(id -> queues.put(id, factory.createQueue(id)));
    }

    private void createConnections() {
        edges.forEach(edge -> {
            if (machines.containsKey(edge.getSource())) {
                machines.get(edge.getSource()).setOutputQueue(queues.get(edge.getTarget()));
            } else if (queues.containsKey(edge.getSource())) {
                machines.get(edge.getTarget()).addInputQueue(queues.get(edge.getSource()));
            }
        });
    }

    public void start() {
        if (products == 0) {
            return;
        }
        machines.values().forEach(Machine::start);
        productGeneratorThread = new Thread(() -> {
            Queue input = queues.get("Input");
            int counter = 1;
            while (products > 0 && isRunning) {
                try {
                    int sleepTime = ThreadLocalRandom.current().nextInt(5000, 25000);
                    System.out.println("Next product coming after " + sleepTime + "ms");
                    Thread.sleep(sleepTime);

                    if (!isRunning) break;

                    input.addProduct(new Product("P" + counter));
                    products--;
                    counter++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        productGeneratorThread.start();
    }

    public void clearSimulation() {
        if (productGeneratorThread != null) {
            productGeneratorThread.interrupt();
            productGeneratorThread = null;
        }
        machines.values().forEach(Machine::stop);
        machines.clear();
        queues.clear();
    }

    private void stopAllMachines() {
        System.out.println("Stopping all machines");
        if (productGeneratorThread != null) {
            productGeneratorThread.interrupt();
            productGeneratorThread = null;
        }
        machines.values().forEach(Machine::stop);
    }

}