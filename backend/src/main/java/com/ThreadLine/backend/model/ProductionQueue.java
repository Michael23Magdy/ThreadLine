package com.ThreadLine.backend.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

public class ProductionQueue implements Publisher{
    @Getter
    private final String id;
    private final Set<Subscriber> subscribers = new HashSet<>();
    private final Queue<Product> itemsQueue;

    public ProductionQueue(int capacity, String id) {
        this.id = id;
        this.itemsQueue = new ArrayBlockingQueue<>(capacity);
    }

    @Override
    public void subscirbe(Subscriber subscriber) {
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscirbe(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    @Override
    public void notifySubscribers() {
        subscribers.forEach((subscriber)->subscriber.update(this.id));
    }

    public synchronized boolean produce(Product product){
        if(itemsQueue.add(product)){
            notifySubscribers();
            return true;
        }
        return false;
    }

    public synchronized Product consume(){
        return itemsQueue.poll();
    }

}
