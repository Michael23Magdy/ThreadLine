package com.ThreadLine.backend.model;

import com.ThreadLine.backend.dto.QueueUpdate;
import com.ThreadLine.backend.exception.internal.QueueOperationException;
import com.ThreadLine.backend.observer.Publisher;
import com.ThreadLine.backend.observer.WebSocketSubscriber;
import lombok.Data;

import java.util.concurrent.LinkedBlockingDeque;

@Data
public class Queue implements Publisher {
    private String id;
    //* built-in thread safety
    private final LinkedBlockingDeque<Product> products = new LinkedBlockingDeque<>();
    private WebSocketSubscriber subscriber;

    public Queue(String id, WebSocketSubscriber subscriber) {
        this.id = id;
        this.subscriber = subscriber;
    }

    public void addProduct(Product product) {
        try {
            notifySubscribers(products.size() + 1);
            products.putFirst(product);
            System.out.println("Added product: " + product.getId() + " to queue: " + id);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QueueOperationException("Failed to add product to queue: " + id);
        }
    }

    public synchronized Product consume() {
        try {
            notifySubscribers(products.size() - 1);
            Product product = products.takeLast();
            System.out.println(Thread.currentThread().getName() + " consumed product: " + product.getId() + " from queue: " + id);
            return product;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new QueueOperationException(" Failed to consume product from queue: " + id);
        }
    }

    public synchronized Product blockingPeek() throws InterruptedException {
        System.out.println(Thread.currentThread().getName() + " is peeking from queue: " + id);
        Product product = products.takeLast();
        products.putLast(product);
        return product;
    }

    private void notifySubscribers(int currentSize) {
        QueueUpdate queueUpdate = new QueueUpdate(id, currentSize);
        subscriber.notify(queueUpdate);
    }
}
