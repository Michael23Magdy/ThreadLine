package com.ThreadLine.backend.observer;

public interface QueuePublisher {
    void subscribe(QueueSubscriber observer);

    void unsubscribe(QueueSubscriber observer);

    void notifySubscribers();
}
