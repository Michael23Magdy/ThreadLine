package com.ThreadLine.backend.model;

public interface Publisher {
    public void subscirbe(Subscriber s);
    public void unsubscirbe(Subscriber s);
    public void notifySubscribers();
}
