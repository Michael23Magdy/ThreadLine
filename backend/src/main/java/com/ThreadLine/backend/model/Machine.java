package com.ThreadLine.backend.model;

import com.ThreadLine.backend.dto.MachineUpdate;
import com.ThreadLine.backend.observer.Publisher;
import com.ThreadLine.backend.observer.WebSocketSubscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Machine implements Runnable, Publisher {

    private String id;
    private List<Queue> inputQueues = new ArrayList<>();
    private Queue outputQueue;
    private Product currentProduct;
    private volatile boolean running = true;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ProductFetcher productFetcher;
    private WebSocketSubscriber subscriber;

    public Machine(String id, WebSocketSubscriber subscriber) {
        this.id = id;
        this.subscriber = subscriber;
    }

    public synchronized void addInputQueue(Queue queue) {
        inputQueues.add(queue);
        productFetcher = new ProductFetcher(inputQueues, executor);
    }

    @Override
    public void run() {
        if (productFetcher == null) {
            productFetcher = new ProductFetcher(inputQueues, executor);
        }
        while (running) {
            try {
                currentProduct = productFetcher.fetchNextProduct();
                processProduct(currentProduct);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Machine " + id + " was interrupted.");
            } catch (Exception e) {
                System.err.println("Error in machine " + id + ": " + e.getMessage());
            }
        }
        System.out.println("Machine " + id + " has stopped.");
    }

    private synchronized void processProduct(Product product) throws InterruptedException, JsonProcessingException {
        int sleepTime = getMachineRunTime();
        System.out.println("Machine " + id + " is processing product " + product.getId() + " for " + sleepTime + "ms");
        notifyWorking();
        Thread.sleep(sleepTime);
        if (outputQueue != null) {
            outputQueue.addProduct(product);
        }
        notifyFinished();
    }

    private void notifyWorking() throws JsonProcessingException {
        MachineUpdate update = new MachineUpdate(id, true, currentProduct.getColor());
        subscriber.notify(update);
    }

    private void notifyFinished() throws JsonProcessingException {
        MachineUpdate update = new MachineUpdate(id, false, null);
        subscriber.notify(update);
    }

    public static int getMachineRunTime() {
        return ThreadLocalRandom.current().nextInt(5000, 25000);
    }

    public void start() {
        executor.execute(this);
    }

    public synchronized void stop() {
        running = false;
        if (productFetcher != null) {
            productFetcher.shutdown();
        }
        executor.shutdown();
    }
}