package com.ThreadLine.backend.observer;

import com.ThreadLine.backend.model.Queue;

public interface QueueSubscriber {
    void onProductAvailable(Queue queue);
}