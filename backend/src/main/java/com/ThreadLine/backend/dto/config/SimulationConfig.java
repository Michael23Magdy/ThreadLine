package com.ThreadLine.backend.dto.config;

import com.ThreadLine.backend.repository.SimulationRepository;
import lombok.Data;

import java.util.List;

@Data
public class SimulationConfig {
    private List<String> machines;
    private List<String> queues;
    private List<Edge> edges;
    private int products;
}