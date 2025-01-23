package com.ThreadLine.backend.model;

import com.ThreadLine.backend.dto.events.MachineUpdate;
import com.ThreadLine.backend.exception.internal.MachineOperationException;
import com.ThreadLine.backend.observer.Publisher;
import com.ThreadLine.backend.observer.WebSocketSubscriber;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Data
public class Machine implements Runnable, Publisher, Cloneable {

    private String id;
    private List<Queue> inputQueues = new ArrayList<>();
    private Queue outputQueue;
    private Product currentProduct;
    private volatile boolean running = true;
    private ExecutorService executor = Executors.newCachedThreadPool();
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
                if (currentProduct == null) {
                    currentProduct = productFetcher.fetchNextProduct();
                }
                processProduct(currentProduct);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new MachineOperationException("Machine " + id + " was interrupted.");
            } catch (Exception e) {
                throw new MachineOperationException("Error in machine " + id + ": " + e.getMessage());
            }
        }
        System.out.println("Machine " + id + " has stopped.");
    }

    private void processProduct(Product product) throws InterruptedException {
        int remainingSleepTime = getMachineRunTime();

        System.out.println("Machine " + id + " is processing product " + product.getId() + " for " + remainingSleepTime + "ms");
        notifyWorking();

        while (running && remainingSleepTime > 0) {
            long sleepInterval = Math.min(100, remainingSleepTime);
            Thread.sleep(sleepInterval);
            remainingSleepTime -= (int) sleepInterval;
        }

        if (running) {
            synchronized (this) {
                if (outputQueue != null) {
                    outputQueue.addProduct(product);
                }
            }
            currentProduct = null;
            notifyFinished();
        }
    }

    private void notifyWorking() {
        MachineUpdate update = new MachineUpdate(id, true, currentProduct.getColor());
        subscriber.notify(update);
    }

    private void notifyFinished() {
        MachineUpdate update = new MachineUpdate(id, false, null);
        subscriber.notify(update);
    }

    public static int getMachineRunTime() {
        return ThreadLocalRandom.current().nextInt(3000, 10000);
    }

    public void start(boolean reset) {
        running = true;
        currentProduct = reset ? null : currentProduct;
        if (executor.isShutdown()) {
            executor = Executors.newCachedThreadPool();
            if (productFetcher != null) {
                productFetcher = new ProductFetcher(inputQueues, executor);
            }
        }
        executor.execute(this);
    }

    public synchronized void stop() {
        System.out.println("Stopping Machine " + id);
        running = false;
        if (productFetcher != null) {
            productFetcher.shutdown();
        }
        executor.shutdownNow();
        Thread.currentThread().interrupt();
    }

    @Override
    public Machine clone() {
        try {
            Machine clone = (Machine) super.clone();
            clone.inputQueues = new ArrayList<>();
            for (Queue queue : inputQueues) {
                clone.inputQueues.add(queue.clone());
            }
            clone.currentProduct = currentProduct == null ? null : currentProduct.clone();
            clone.executor = Executors.newCachedThreadPool();
            clone.productFetcher = new ProductFetcher(clone.inputQueues, clone.executor);
            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Failed to clone Machine", e);
        }
    }
}