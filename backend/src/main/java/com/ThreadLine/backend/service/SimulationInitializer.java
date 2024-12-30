package com.ThreadLine.backend.service;

import com.ThreadLine.backend.config.Factory;
import com.ThreadLine.backend.model.Machine;
import com.ThreadLine.backend.model.Product;
import com.ThreadLine.backend.model.Queue;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class SimulationInitializer {
    private final Factory factory;

    public SimulationInitializer(Factory factory) {
        this.factory = factory;
    }

    public void initialize() {

    }

    public void start() {
        Queue q1 = factory.createQueue("Q1");
        Queue q2 = factory.createQueue("Q2");
        Queue q3 = factory.createQueue("Q3");
        Queue q4 = factory.createQueue("Q4");

        Machine m1 = factory.createMachine("M1");
        Machine m2 = factory.createMachine("M2");
        Machine m3 = factory.createMachine("M3");

        m1.addInputQueue(q1);
        m1.setOutputQueue(q2);

        m2.addInputQueue(q1);
        m2.setOutputQueue(q3);

        m3.addInputQueue(q2);
        m3.addInputQueue(q3);
        m3.setOutputQueue(q4);

        m1.start();
        m2.start();
        m3.start();

        Thread obj = new Thread();
        for (int i = 1; i <= 5; i++) {
            int sleepTime = ThreadLocalRandom.current().nextInt(5000, 10000);
            System.out.println("Next product coming after " + sleepTime + "ms");
            try {
                obj.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            q1.addProduct(new Product("P" + i));
        }
    }
}
