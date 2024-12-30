package com.ThreadLine.backend.model;

import com.ThreadLine.backend.dto.MachineUpdate;
import com.ThreadLine.backend.observer.Publisher;
import com.ThreadLine.backend.observer.Subscriber;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class Machine implements Runnable, Publisher {

    private String id;
    private List<Queue> inputQueues = new ArrayList<>();
    private Queue outputQueue;
    private Product currentProduct;
    private volatile boolean running = true;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ProductFetcher productFetcher;
    private Subscriber subscriber;

    public String getId() {
        return id;
    }

    public void setOutputQueue(Queue outputQueue) {
        this.outputQueue = outputQueue;
    }

    public Machine(String id, Subscriber subscriber) {
        this.id = id;
        this.subscriber = subscriber;
    }

    public synchronized void addInputQueue(Queue queue) {
        inputQueues.add(queue);
        queue.addConsumer(this);
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
                System.out.println("Machine " + id + " is processing product " + currentProduct.getId());
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
        notifyWorking();
        int sleepTime = getMachineRunTime();
        System.out.println("Machine " + id + " is processing product " + product.getId() + " for " + sleepTime + "ms");
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
        return ThreadLocalRandom.current().nextInt(5000, 10000);
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