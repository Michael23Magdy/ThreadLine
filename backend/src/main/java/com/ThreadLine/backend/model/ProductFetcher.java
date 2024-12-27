package com.ThreadLine.backend.model;

import java.util.ArrayList;
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
        List<Future<Product>> futures = new ArrayList<>();
        for (Queue queue : inputQueues) {
            Future<Product> future = executor.submit(queue::consume);
            futures.add(future);
        }

        ExecutorCompletionService<Product> completionService = new ExecutorCompletionService<>(executor);
        for (Future<Product> future : futures) {
            completionService.submit(future::get); //* wait for future.get()
        }
        try {
            //* take() waits for future completion
            Future<Product> completedFuture = completionService.take();
            Product product = completedFuture.get();

            //! cancel other futures
            for (Future<Product> future : futures) {
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
            return product;
        } catch (ExecutionException e) {
            throw new RuntimeException("Error fetching product", e);
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
