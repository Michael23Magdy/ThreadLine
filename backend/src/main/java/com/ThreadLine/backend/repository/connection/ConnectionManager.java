package com.ThreadLine.backend.repository.connection;

import com.ThreadLine.backend.config.Factory;
import com.ThreadLine.backend.dto.config.Edge;
import com.ThreadLine.backend.repository.state.MachineStateHolder;
import com.ThreadLine.backend.repository.state.QueueStateHolder;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConnectionManager {
    private final Factory factory;
    @Setter
    private MachineStateHolder machinesState;
    @Setter
    private QueueStateHolder queuesState;

    public ConnectionManager(Factory factory, MachineStateHolder machinesState, QueueStateHolder queuesState) {
        this.factory = factory;
        this.machinesState = machinesState;
        this.queuesState = queuesState;
    }

    public ConnectionManager createMachines(List<String> machineIds) {
        machineIds.forEach(id ->
                machinesState.addMachine(id, factory.createMachine(id)));
        return this;
    }

    public ConnectionManager createQueues(List<String> queueIds) {
        queueIds.forEach(id ->
                queuesState.addQueue(id, factory.createQueue(id)));
        return this;

    }

    public void createConnections(List<Edge> edges) {
        edges.forEach(edge -> {
            if (machinesState.hasMachine(edge.getSource())) {
                machinesState.getMachine(edge.getSource())
                        .setOutputQueue(queuesState.getQueue(edge.getTarget()));
            } else if (queuesState.hasQueue(edge.getSource())) {
                machinesState.getMachine(edge.getTarget())
                        .addInputQueue(queuesState.getQueue(edge.getSource()));
            }
        });
    }
}
