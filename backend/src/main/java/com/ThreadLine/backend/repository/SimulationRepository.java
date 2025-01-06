package com.ThreadLine.backend.repository;

import com.ThreadLine.backend.dto.config.SimulationConfig;
import com.ThreadLine.backend.model.Product;
import com.ThreadLine.backend.model.Queue;
import com.ThreadLine.backend.repository.connection.ConnectionManager;
import com.ThreadLine.backend.repository.state.SimulationStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ThreadLocalRandom;

@Repository
@RequiredArgsConstructor
public class SimulationRepository {
    private final ConnectionManager connectionManager;
    private final SimulationStateManager stateManager;
    private volatile Boolean isRunning = true;
    private Thread productGeneratorThread;


    public void pause() {
        isRunning = false;
        stateManager.savePauseState();
        stopAllMachines();
    }

    public void resume() {
        isRunning = true;
        stateManager.restorePauseState();
        start(false);
    }

    public void replay(int modifiedProductsCount) {
        stopAllMachines();
        isRunning = true;
        stateManager.restoreInitialState(modifiedProductsCount);
        start(true);
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

    public void start(boolean reset) {
        if (stateManager.getProductCount() == 0) {
            return;
        }
        stateManager.getMachinesState().startAllMachines(reset);
        productGeneratorThread = new Thread(() -> {
            Queue input = stateManager.getQueuesState().getInputQueue();
            int counter = 1;
            while (stateManager.getProductCount() > 0 && isRunning) {
                try {
                    int sleepTime = ThreadLocalRandom.current().nextInt(5000, 25000);
                    System.out.println("Next product coming after " + sleepTime + "ms");
                    Thread.sleep(sleepTime);

                    if (!isRunning) break;

                    input.addProduct(new Product("P" + counter));
                    stateManager.setProductCount(stateManager.getProductCount() - 1);
                    counter++;
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        productGeneratorThread.start();
    }

    public void clearSimulation() {
        if (productGeneratorThread != null) {
            productGeneratorThread.interrupt();
            productGeneratorThread = null;
        }
        stateManager.clearState();
    }

    private void stopAllMachines() {
        System.out.println("Stopping all machines");
        if (productGeneratorThread != null) {
            productGeneratorThread.interrupt();
            productGeneratorThread = null;
        }
        stateManager.getMachinesState().stopAllMachines();
    }
}