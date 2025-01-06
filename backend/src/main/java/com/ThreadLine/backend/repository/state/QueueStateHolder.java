package com.ThreadLine.backend.repository.state;

import com.ThreadLine.backend.model.Queue;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class QueueStateHolder {
    private final Map<String, Queue> queues = new HashMap<>();

    public void addQueue(String id, Queue queue) {
        queues.put(id, queue);
    }

    public Queue getQueue(String id) {
        return queues.get(id);
    }

    public Queue getInputQueue() {
        return queues.get("Input");
    }

    public boolean hasQueue(String id) {
        return queues.containsKey(id);
    }

    public void clear() {
        queues.clear();
    }

    public Map<String, Queue> getQueuesCopy() {
        return queues
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone()
                ));
    }

    public void restore(Map<String, Queue> queuesState) {
        clear();
        queues.putAll(queuesState
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone()
                ))
        );
    }
}
