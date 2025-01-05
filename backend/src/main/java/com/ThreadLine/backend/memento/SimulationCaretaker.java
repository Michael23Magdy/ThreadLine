package com.ThreadLine.backend.memento;

import lombok.Getter;

@Getter
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
