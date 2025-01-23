package com.ThreadLine.backend.repository;

import com.ThreadLine.backend.dto.config.SimulationConfig;
import com.ThreadLine.backend.repository.connection.ConnectionManager;
import com.ThreadLine.backend.repository.executor.SimulationExecutor;
import com.ThreadLine.backend.repository.state.SimulationStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class SimulationRepository {
    private final ConnectionManager connectionManager;
    private final SimulationStateManager stateManager;
    private final SimulationExecutor executor;


    public void pause() {
        stateManager.savePauseState();
        executor.pause();
    }

    public void resume() {
        stateManager.restorePauseState();
        executor.resume();
    }

    public void replay(int modifiedProductsCount) {
        executor.pause();
        stateManager.restoreInitialState(modifiedProductsCount);
        executor.start(true);
    }

    public SimulationRepository initialize(SimulationConfig config) {
        clearSimulation();
        connectionManager
                .createMachines(config.getMachines())
                .createQueues(config.getQueues())
                .createConnections(config.getEdges());
        stateManager.setProductCount(config.getProducts());
        stateManager.setCurrentEdges(config.getEdges());
        stateManager.saveInitialState();
        return this;
    }

    public void start() {
        executor.start(true);
    }

    public void clearSimulation() {
        executor.pause();
        stateManager.clearState();
    }
}