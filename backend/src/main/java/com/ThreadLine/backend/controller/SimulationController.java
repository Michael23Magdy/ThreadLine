package com.ThreadLine.backend.controller;

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
        simulationRepository.initialize(config).start();
        return ResponseEntity.ok().build();
    }
}