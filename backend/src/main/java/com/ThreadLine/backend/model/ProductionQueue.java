package com.ThreadLine.backend.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class ProductionQueue implements Publisher{
    @Getter
    private final String id;
    private final Set<Subscriber> subscribers = new HashSet<>();
    private final Queue<Product> itemsQueue;

    public ProductionQueue(String id) {
        this.id = id;
        this.itemsQueue = new LinkedList<>();
    }

    @Override
    public void subscribe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach((subscriber)->subscriber.update(this));
    }

    public synchronized void produce(Product product){
        itemsQueue.add(product);
        notifySubscribers();
    }

    public synchronized Product consume(){
        return itemsQueue.poll();
    }

}
