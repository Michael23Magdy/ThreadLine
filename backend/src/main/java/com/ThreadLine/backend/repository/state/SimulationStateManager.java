package com.ThreadLine.backend.repository.state;

import com.ThreadLine.backend.dto.config.Edge;
import com.ThreadLine.backend.memento.SimulationCaretaker;
import com.ThreadLine.backend.memento.SimulationMemento;
import com.ThreadLine.backend.repository.connection.ConnectionManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SimulationStateManager {
    private final SimulationCaretaker caretaker;
    @Getter
    private final MachineStateHolder machinesState;
    @Getter
    private final QueueStateHolder queuesState;
    private final ConnectionManager connectionManager;
    @Setter
    private List<Edge> currentEdges = new ArrayList<>();
    @Getter
    @Setter
    private int productCount;

    public void clearState() {
        machinesState.clear();
        queuesState.clear();
    }

    public void saveInitialState() {
        caretaker.saveStartMemento(createMemento());
    }

    public void savePauseState() {
        caretaker.savePauseMemento(createMemento());
    }

    public void restorePauseState() {
        SimulationMemento memento = caretaker.getPauseMemento();
        if (memento != null) {
            restoreFromMemento(memento, null);
        }
    }

    public void restoreInitialState(Integer modifiedProductsCount) {
        SimulationMemento memento = caretaker.getStartMemento();
        if (memento != null) {
            restoreFromMemento(memento, modifiedProductsCount);
        }
    }

    private SimulationMemento createMemento() {
        return new SimulationMemento(
                machinesState.getMachinesCopy(),
                queuesState.getQueuesCopy(),
                productCount
        );
    }

    private void restoreFromMemento(SimulationMemento memento, Integer modifiedProductsCount) {
        machinesState.restore(memento.machines());
        queuesState.restore(memento.queues());
        connectionManager.setMachinesState(machinesState);
        connectionManager.setQueuesState(queuesState);
        connectionManager.createConnections(currentEdges);
        this.productCount = modifiedProductsCount != null ? modifiedProductsCount : memento.products();
    }
}