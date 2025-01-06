package com.ThreadLine.backend.repository.state;

import com.ThreadLine.backend.model.Machine;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MachineStateHolder {
    private final Map<String, Machine> machines = new HashMap<>();

    public void addMachine(String id, Machine machine) {
        machines.put(id, machine);
    }

    public Machine getMachine(String id) {
        return machines.get(id);
    }

    public boolean hasMachine(String id) {
        return machines.containsKey(id);
    }

    public void startAllMachines(boolean reset) {
        machines.values().forEach(machine -> machine.start(reset));
    }

    public void stopAllMachines() {
        machines.values().forEach(Machine::stop);
    }

    public void clear() {
        stopAllMachines();
        machines.clear();
    }

    public Map<String, Machine> getMachinesCopy() {
        return machines
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone()
                ));
    }

    public void restore(Map<String, Machine> machinesState){
        clear();
        machines.putAll(machinesState
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().clone()
                )));
    }
}
