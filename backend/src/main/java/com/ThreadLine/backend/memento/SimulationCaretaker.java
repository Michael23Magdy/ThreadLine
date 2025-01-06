package com.ThreadLine.backend.memento;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class SimulationCaretaker {
    private SimulationMemento startMemento;
    private SimulationMemento pauseMemento;

    public void saveStartMemento(SimulationMemento memento) {
        this.startMemento = memento;
    }
    public void savePauseMemento(SimulationMemento memento) {
        this.pauseMemento = memento;
    }
}
