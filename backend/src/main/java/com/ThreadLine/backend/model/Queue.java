package com.ThreadLine.backend.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public class Queue {
    private String id;
    //* built-in thread safety
    private final LinkedBlockingDeque<Product> products = new LinkedBlockingDeque<>();
    private Set<Machine> machines = new HashSet<>();

    public Queue(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public synchronized void addConsumer(Machine machine) {
        machines.add(machine);
    }

    public synchronized void removeConsumer(Machine machine) {
        machines.remove(machine);
    }

    public void addProduct(Product product) {
        try {
            products.putFirst(product);
            System.out.println("Added product: " + product.getId() + " to queue: " + id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to add product to queue: " + id);
        }
    }

    public Product consume() {
        try {
            Product product = products.takeLast();
            System.out.println("Consumed product: " + product.getId() + " from queue: " + id);
            return product;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " Failed to consume product from queue: " + id);
            return null;
        }
    }

    public synchronized Product blockingPeek() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is peeking from queue: " + id);
        Product product = products.takeLast();
        products.putLast(product);
        return product;
    }
}
