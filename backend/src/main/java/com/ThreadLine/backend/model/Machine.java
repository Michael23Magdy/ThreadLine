package com.ThreadLine.backend.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Machine implements Runnable {

    private String id;
    private List<Queue> inputQueues = new ArrayList<>();
    private Queue outputQueue;
    private Product currentProduct;
    private volatile boolean ready = true;
    private volatile boolean processing = false;
    private int currentQueueIndex = 0;

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
    }

    @Override
    public void run() {
        while (ready) {
            try {
                Product productToProcess = waitForProduct();
                if (productToProcess != null) {
                    System.out.println("Machine " + id + " is processing product " + productToProcess.getId());
                    processProduct(productToProcess);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Machine " + id + " is stopping.");
    }

    private synchronized Product waitForProduct() throws InterruptedException {
        if (!ready) {
            return null;
        }
        currentProduct = fetchNextProduct();
        if (currentProduct != null) {
            processing = true;
        }
        return currentProduct;
    }

    private synchronized void processProduct(Product product) throws InterruptedException {
        int sleepTime = getMachineRunTime();
        System.out.println("Machine " + id + " is processing product " + product.getId() + " for " + sleepTime + "ms");
        Thread.sleep(sleepTime);
        if (outputQueue != null) {
            outputQueue.addProduct(product);
        }
        currentProduct = null;
        processing = false;
        notifyAll();
    }

    private Product fetchNextProduct() {
        if (inputQueues.isEmpty())
            return null;

        //* Round-robin selection
        int attempts = 0;
        while (attempts < inputQueues.size()) {
            currentQueueIndex = (currentQueueIndex + 1) % inputQueues.size();
            Queue queue = inputQueues.get(currentQueueIndex);
            if (!queue.isEmpty()) {
                return queue.consume();
            }
            attempts++;
        }
        return null;
    }

    public static int getMachineRunTime() {
        return ThreadLocalRandom.current().nextInt(5000, 10000);
    }

    public void start() {
        Thread thread = new Thread(this, id);
        thread.start();
    }

    public synchronized void stop() {
        ready = false;
        notifyAll();
    }

    public synchronized boolean isReady() {
        return !ready && currentProduct == null;
    }
}
