package com.ThreadLine.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

public class ProductFetcher {

    private final List<Queue> inputQueues;
    private final ExecutorService executor;

    public ProductFetcher(List<Queue> inputQueues, ExecutorService executor) {
        this.inputQueues = inputQueues;
        this.executor = executor;
    }

    public Product fetchNextProduct() throws InterruptedException {
        AtomicReference<Product> fetchedProduct = new AtomicReference<>(null);
        CountDownLatch productFound = new CountDownLatch(1);
        List<Future<?>> futures = new ArrayList<>();
        for (Queue queue : inputQueues) {
            Future<?> future = executor.submit(() -> {
                Product product = null;
                try {
                    product = queue.blockingPeek();
                } catch (InterruptedException e) {
                    System.out.println("couldn't peek from queue " + queue.getId());
                    Thread.currentThread().interrupt();
                }
                if (product != null && fetchedProduct.compareAndSet(null, product)) {
                    fetchedProduct.set(queue.consume());
                    productFound.countDown();
                }
            });
            futures.add(future);
        }

        productFound.await();

        for (Future<?> future : futures) {
            future.cancel(true);
        }
        return fetchedProduct.get();
    }

    public void shutdown() {
        executor.shutdown();
    }
}
