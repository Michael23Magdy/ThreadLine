package com.ThreadLine.backend.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Machine implements Runnable, Subscriber{
    private volatile boolean processing;
    private boolean ready;
    private final Map<String, ProductionQueue> inputQueues;
    private final ProductionQueue outputQueue;
    private Product currentProduct;
    private Thread thread;

    public Machine(List<ProductionQueue> inputQueues, ProductionQueue outputQueue) {
        this.processing = false;
        this.ready = true;
        this.outputQueue = outputQueue;
        this.inputQueues = new HashMap<>();
        inputQueues.forEach((inputQueue)->
                this.inputQueues.put(inputQueue.getId(), inputQueue));
    }

    @Override
    public void run() {
        while (ready){
            search();
            if (!ready && processing) {
                process();
            }
            if(!ready && !processing) {
                produce();
            }
        }
    }

    public synchronized void start() {
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stopThread() {
        ready = false; // Set the flag to false to stop the thread
        if (thread != null) {
            thread.interrupt(); // Interrupt the thread if it's waiting or sleeping
        }
    }


    @Override
    public synchronized void updateFromInput(String id) {
        if(ready){
            currentProduct = inputQueues.get(id).consume();
            if(currentProduct == null){
                return;
            }
            ready = false;
            processing = true;
        }
        else
            inputQueues.get(id).unsubscirbe(this);
    }

    @Override
    public synchronized void updateFromOutput(){
        if (!processing && !ready)
            produce();
        else
            outputQueue.unsubscirbe(this);
    }

    private void process(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        processing = false;
    }

    private void search(){
        for(Map.Entry<String, ProductionQueue> entry : inputQueues.entrySet()){
            ProductionQueue inputQueue = entry.getValue();
            currentProduct = inputQueue.consume();
            if(currentProduct == null){
                inputQueue.subscirbe(this);
                continue;
            }
            ready = false;
            processing = true;
            break;
        }
    }

    private void produce(){
        if(outputQueue.produce(currentProduct)){
            currentProduct=null;
            ready = true;
            processing = false;
            return;
        };
        outputQueue.subscirbe(this);
        stopThread();
    }
}
