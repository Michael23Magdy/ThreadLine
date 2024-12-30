package com.ThreadLine.backend.model;

import com.ThreadLine.backend.dto.QueueUpdate;
import com.ThreadLine.backend.observer.Publisher;
import com.ThreadLine.backend.observer.Subscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

@Data
public class Queue implements Publisher {
    private String id;
    //* built-in thread safety
    private final LinkedBlockingDeque<Product> products = new LinkedBlockingDeque<>();
    private Set<Machine> machines = new HashSet<>();
    private Subscriber subscriber;

    public Queue(String id, Subscriber subscriber) {
        this.id = id;
        this.subscriber = subscriber;
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
            notifySubscribers();
            System.out.println("Added product: " + product.getId() + " to queue: " + id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to add product to queue: " + id);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Product consume() {
        try {
            Product product = products.takeLast();
            notifySubscribers();
            System.out.println(Thread.currentThread().getName() + " consumed product: " + product.getId() + " from queue: " + id);
            return product;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(Thread.currentThread().getName() + " Failed to consume product from queue: " + id);
            return null;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Product blockingPeek() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is peeking from queue: " + id);
        Product product = products.takeLast();
        products.putLast(product);
        return product;
    }

    private void notifySubscribers() throws JsonProcessingException {
        QueueUpdate queueUpdate = new QueueUpdate(id, products.size());
        subscriber.notify(queueUpdate);
    }
}
