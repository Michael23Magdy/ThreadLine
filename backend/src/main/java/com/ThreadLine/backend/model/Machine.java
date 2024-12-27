package com.ThreadLine.backend.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Machine implements Runnable {

    private String id;
    private List<Queue> inputQueues = new ArrayList<>();
    private Queue outputQueue;
    private Product currentProduct;
    private volatile boolean ready = true;
    private volatile boolean running = true;
    private int currentQueueIndex = 0;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private ProductFetcher productFetcher;

    public String getId() {
        return id;
    }

    public void setOutputQueue(Queue outputQueue) {
        this.outputQueue = outputQueue;
    }

    public Machine(String id) {
        this.id = id;
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
                Product product = productFetcher.fetchNextProduct();
                System.out.println("Machine " + id + " is processing product " + product.getId());
                processProduct(product);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Machine " + id + " was interrupted.");
            } catch (Exception e) {
                System.err.println("Error in machine " + id + ": " + e.getMessage());
            }
        }
        System.out.println("Machine " + id + " has stopped.");
    }

    private synchronized void processProduct(Product product) throws InterruptedException {
        int sleepTime = getMachineRunTime();
        System.out.println("Machine " + id + " is processing product " + product.getId() + " for " + sleepTime + "ms");
        Thread.sleep(sleepTime);
        if (outputQueue != null) {
            outputQueue.addProduct(product);
        }
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
