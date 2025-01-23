package com.ThreadLine.backend.repository.executor;

import com.ThreadLine.backend.model.Product;
import com.ThreadLine.backend.model.Queue;
import com.ThreadLine.backend.repository.state.SimulationStateManager;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class ProductGeneratorThread extends Thread {
    private final Queue inputQueue;
    private final SimulationStateManager stateManager;
    private final Supplier<Boolean> isRunningSupplier;
    private int counter = 1;

    @Override
    public void run() {
        while (stateManager.getProductCount() > 0 && isRunningSupplier.get()) {
            try {
                int sleepTime = ThreadLocalRandom.current().nextInt(3000, 7000);
                System.out.println("Next product coming after " + sleepTime + " ms");
                Thread.sleep(sleepTime);

                if (!isRunningSupplier.get()) break;

                inputQueue.addProduct(new Product("P" + counter));
                stateManager.setProductCount(stateManager.getProductCount() - 1);
                counter++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
