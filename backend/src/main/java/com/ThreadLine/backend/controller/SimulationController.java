package com.ThreadLine.backend.controller;

import com.ThreadLine.backend.dto.config.SimulationConfig;
import com.ThreadLine.backend.exception.internal.InvalidSimulationConfigException;
import com.ThreadLine.backend.repository.SimulationRepository;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/simulation")
public class SimulationController {
    private final SimulationRepository simulationRepository;

    public SimulationController(SimulationRepository simulationRepository) {
        this.simulationRepository = simulationRepository;
    }

    @PostMapping("/start")
    public ResponseEntity<?> initializeSimulation(@RequestBody SimulationConfig config) {
        if (config.getMachines() == null || config.getMachines().isEmpty()) {
            throw new InvalidSimulationConfigException("Machines list cannot be empty");
        }
        if (config.getQueues() == null || config.getQueues().isEmpty()) {
            throw new InvalidSimulationConfigException("Queues list cannot be empty");
        }
        if (config.getEdges() == null || config.getEdges().isEmpty()) {
            throw new InvalidSimulationConfigException("Edges list cannot be empty");
        }
        simulationRepository.initialize(config).start(true);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/replay")
    public ResponseEntity<?> replaySimulation(@RequestParam(name = "products", required = true) int products) {
        simulationRepository.replay(products);
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

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearSimulation() {
        simulationRepository.clearSimulation();
        return ResponseEntity.ok().build();
    }
}