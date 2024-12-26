package com.ThreadLine.backend.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ProductFetcher {

    private final List<Queue> inputQueues;
    private final ExecutorService executor;

    public ProductFetcher(List<Queue> inputQueues, ExecutorService executor) {
        this.inputQueues = inputQueues;
        this.executor = executor;
    }

    public Product fetchNextProduct() throws InterruptedException {
        // Shared result and control flags
        AtomicReference<Product> result = new AtomicReference<>(null);
        AtomicBoolean productFound = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(inputQueues.size());

        for (Queue queue : inputQueues) {
            executor.submit(() -> {
                try {
                    if (productFound.get()) {
                        return; // Exit early if a product is found
                    }

                    Product product = null;
                    synchronized (queue) {
                        if (!queue.isEmpty()) {
                            product = queue.consume(); // Consume a product if available
                        }
                    }

                    if (product != null && productFound.compareAndSet(false, true)) {
                        result.set(product); // Set the found product
                    }
                } finally {
                    latch.countDown(); // Mark this thread as finished
                }
            });
        }

        // Wait for threads to finish or a product to be found
        latch.await();
        return result.get();
    }

    public void shutdown() {
        executor.shutdown();
    }
}
