package com.ThreadLine.backend.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Queue {
    private String id;
    private List<Product> products = new ArrayList<>();
    private Set<Machine> machines = new HashSet<>();

    public Queue(String id) {
        this.id = id;
    }

    public synchronized void addConsumer(Machine machine) {
        System.out.println("Adding consumer: " + machine.getId() + " for queue: " + id);
        machines.add(machine);
    }

    public synchronized void removeConsumer(Machine machine) {
        machines.remove(machine);
    }

    public synchronized void addProduct(Product product) {
        System.out.println("Adding product: " + product.getId() + " to queue: " + id);
        products.add(product);
        notifyAll();
    }

    public synchronized Product consume() {
        while (products.isEmpty()) {
            try {
                System.out.println("No products available for Machine " + Thread.currentThread().getName() + " for queue: " + id);
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
                return null;
            }
        }
        notifyAll();
        return products.removeFirst();
    }

    public synchronized boolean isEmpty() {
        System.out.println("No products available for Machine " + Thread.currentThread().getName() + " for queue: " + id);
        return products.isEmpty();
    }

    public synchronized int size() {
        return products.size();
    }
}
