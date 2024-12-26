package com.ThreadLine.backend;

import com.ThreadLine.backend.model.Machine;
import com.ThreadLine.backend.model.Product;
import com.ThreadLine.backend.model.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) throws InterruptedException {
//        SpringApplication.run(BackendApplication.class, args);

        // Setup simulation
        Queue q1 = new Queue("Q1");
        Queue q2 = new Queue("Q2");
        Queue q3 = new Queue("Q3");
        Queue q4 = new Queue("Q4");

        Machine m1 = new Machine("M1");
        Machine m2 = new Machine("M2");
        Machine m3 = new Machine("M3");

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
            obj.sleep(sleepTime);
            q1.addProduct(new Product("P" + i));
        }

    }

}
