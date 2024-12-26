package com.ThreadLine.backend.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Machine implements Runnable, Subscriber {
    private volatile boolean ready = true;
    private final Map<String, ProductionQueue> inputQueues;
    private final ProductionQueue outputQueue;
    private Product currentProduct;
    private Thread thread;

    public Machine(List<ProductionQueue> inputQueues, ProductionQueue outputQueue) {
        this.outputQueue = outputQueue;
        this.inputQueues = new HashMap<>();
        inputQueues.forEach((inputQueue) ->
                this.inputQueues.put(inputQueue.getId(), inputQueue));
    }

    @Override
    public void run() {
        while (true) {
            if (ready) {
                search();
                stopThread();
            } else {
                try {
                    Thread.sleep(getMchineRunTime());
                    outputQueue.produce(currentProduct);
                    currentProduct = null;
                    ready = true;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    @Override
    public synchronized void update(ProductionQueue inputQueue) {
        if (ready) {
            currentProduct = inputQueue.consume();
            if (currentProduct == null) {
                return;
            }
            start();
            ready = false;
        } else
            inputQueue.unsubscribe(this);
    }


    private void search() {
        for (Map.Entry<String, ProductionQueue> entry : inputQueues.entrySet()) {
            ProductionQueue inputQueue = entry.getValue();
            currentProduct = inputQueue.consume();
            if (currentProduct == null) {
                inputQueue.subscribe(this);
                continue;
            }
            ready = false;
            break;
        }
    }

    public static int getMchineRunTime() {
        return ThreadLocalRandom.current().nextInt(5000, 25001);
    }


    public synchronized void start() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stopThread() {
        ready = true; // Set the flag to false to stop the thread
        if (thread != null) {
            thread.interrupt(); // Interrupt the thread if it's waiting or sleeping
        }
    }
}
