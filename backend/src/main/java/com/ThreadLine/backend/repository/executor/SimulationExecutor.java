package com.ThreadLine.backend.repository.executor;

import com.ThreadLine.backend.model.Queue;
import com.ThreadLine.backend.repository.state.MachineStateHolder;
import com.ThreadLine.backend.repository.state.QueueStateHolder;
import com.ThreadLine.backend.repository.state.SimulationStateManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class SimulationExecutor {
    private final MachineStateHolder machinesState;
    private final QueueStateHolder queuesState;
    private final SimulationStateManager stateManager;
    private volatile boolean isRunning = true;
    private Thread productGeneratorThread;

    public void start(boolean reset) {
        if (stateManager.getProductCount() == 0) return;
        isRunning = true;
        machinesState.startAllMachines(reset);
        startProductGenerator();
    }

    public void pause(){
        isRunning = false;
        stopProductGenerator();
        machinesState.stopAllMachines();
    }

    public void resume(){
        isRunning = true;
        start(false);
    }

    private void startProductGenerator() {
        productGeneratorThread = new ProductGeneratorThread(
                queuesState.getInputQueue(),
                stateManager,
                () -> isRunning
        );
        productGeneratorThread.start();
    }

    private void stopProductGenerator(){
        if(productGeneratorThread != null){
            productGeneratorThread.interrupt();
            productGeneratorThread = null;
        }
    }
}