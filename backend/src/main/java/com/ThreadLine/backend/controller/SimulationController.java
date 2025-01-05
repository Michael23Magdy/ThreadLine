package com.ThreadLine.backend.controller;

import com.ThreadLine.backend.exception.internal.InvalidSimulationConfigException;
import com.ThreadLine.backend.repository.SimulationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    private final SimulationRepository simulationRepository;

    public SimulationController(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<?> initializeSimulation(@RequestBody SimulationRepository.SimulationConfig config) {
        if (config.getMachines() == null || config.getMachines().isEmpty()) {
            throw new InvalidSimulationConfigException("Machines list cannot be empty");
        }
        if (config.getQueues() == null || config.getQueues().isEmpty()) {
            throw new InvalidSimulationConfigException("Queues list cannot be empty");
        }
        if (config.getEdges() == null || config.getEdges().isEmpty()) {
            throw new InvalidSimulationConfigException("Edges list cannot be empty");
        }
        simulationRepository.initialize(config).start();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/replay")
    public ResponseEntity<?> replaySimulation() {
        simulationRepository.replay();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/pause")
    public ResponseEntity<?> pauseSimulation() {
        simulationRepository.pause();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resume")
    public ResponseEntity<?> resumeSimulation() {
        simulationRepository.resume();
        return ResponseEntity.ok().build();
    }
}