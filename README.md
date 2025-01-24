****# ThreadLine: Producer/Consumer Simulation System 🏭

<div align="center">


A real-time assembly line simulation system featuring multi-threaded processing, dynamic visualization, and interactive configuration.

[![Java](https://img.shields.io/badge/Java-21-red.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)

[Demo Video](#-demo) | [Architecture](#-architecture)

</div>

## ✨ Features

- **Real-time Visualization** 🎯
  - Dynamic machine state updates
  - Live queue capacity monitoring
  - Product flow animation
  - Audio feedback for completed products

- **Interactive Configuration** 🛠
  - Drag-and-drop interface
  - Visual connection management
  - Real-time validation

- **Concurrent Processing** ⚡
  - Multi-threaded machine operations
  - Thread-safe queue management
  - Synchronized state handling

- **Simulation Controls** 🎮
  - Play/Pause functionality
  - State replay capability
  - Clear simulation option

## 🎥 Demo

<div align="center">
<video controls>
  <source src="./out/media/DEMO.mp4" type="video/mp4">
</video>
    <!-- ![Demo]() -->
</div>
![Running Simulation](placeholder-for-running-simulation.png)

</div>

## 🏗 Architecture

### System Design (UML Class Diagram)

<details>
<summary>Click to expand UML Diagram</summary>

```plantuml
@startuml

skinparam class {
    BackgroundColor White
    ArrowColor Black
    BorderColor Black
}

skinparam stereotypeCBackgroundColor LightBlue
skinparam packageBackgroundColor LightGray

package "Observer Pattern" {
    interface Publisher {
        + attach(subscriber: WebSocketSubscriber)
        + detach(subscriber: WebSocketSubscriber)
        + notifySubscribers()
    }

    interface WebSocketSubscriber {
        + notify(event: SimulationEvent)
    }

    class WebSocketService {
        - simulationController: WebSocketController
        - messageQueue: BlockingQueue<SimulationEvent>
        + notify(event: SimulationEvent)
        + init()
    }
}

package "Factory Pattern" {
    class Factory {
        - webSocketService: WebSocketService
        + createMachine(id: String): Machine
        + createQueue(id: String): Queue
    }
}

package "Prototype Pattern" {
    interface Cloneable {
        + clone(): Object
    }

    class Machine {
        - id: String
        - inputQueues: List<Queue>
        - outputQueue: Queue
        - currentProduct: Product
        - running: boolean
        + addInputQueue(queue: Queue)
        + run()
        + start(boolean)
        + stop()
        + clone(): Machine
    }

    class Queue {
        - id: String
        - products: LinkedBlockingDeque<Product>
        + addProduct(product: Product)
        + consume(): Product
        + clone(): Queue
    }

    class Product {
        - id: String
        - color: String
        - generateRandomColor()
        + clone(): Product
    }
}

package "Memento Pattern" {
    class SimulationCaretaker {
        - startMemento: SimulationMemento
        - pauseMemento: SimulationMemento
        + saveStartMemento(memento: SimulationMemento)
        + savePauseMemento(memento: SimulationMemento)
        + getStartMemento(): SimulationMemento
        + getPauseMemento(): SimulationMemento
    }

    class SimulationMemento {
        - machines: Map<String,Machine>
        - queues: Map<String,Queue>
        - products: int
    }

    class SimulationStateManager {
        - caretaker: SimulationCaretaker
        - machinesState: MachineStateHolder
        - queuesState: QueueStateHolder
        + saveInitialState()****
        + savePauseState()
        + restoreInitialState(products: Integer)
        + restorePauseState()
    }
}

package "Core Simulation" {
    class SimulationRepository {
        - connectionManager: ConnectionManager
        - stateManager: SimulationStateManager
        - executor: SimulationExecutor
        + initialize(**config**: SimulationConfig)
        + start()
        + pause()
        + resume()
        + replay(products: int)
    }

    class ConnectionManager {
        - factory: Factory
        - machinesState: MachineStateHolder
        - queuesState: QueueStateHolder
        + createMachines(machines: List<String>)
        + createQueues(queues: List<String>)
        + createConnections(edges: List<Edge>)
    }
}

' Relationships
Publisher <|.. Machine
Publisher <|.. Queue
WebSocketSubscriber <|.. WebSocketService
Cloneable <|.. Machine
Cloneable <|.. Queue
Cloneable <|.. Product

SimulationStateManager --> SimulationCaretaker
SimulationCaretaker --> SimulationMemento
SimulationRepository --> SimulationStateManager
SimulationRepository --> ConnectionManager
ConnectionManager --> Factory

Factory ..> Machine: creates
Factory ..> Queue: creates
Machine --> Queue: uses
Queue --> Product: contains

@enduml
```

</details>

## 🛠 Tech Stack

### Backend
- Java 21
- Spring Boot
- WebSocket
- Lombok

### Frontend
- React.js
- React Flow
- StompJS
- Context API

## 🚀 Getting Started

### Prerequisites
```bash
java 21
maven
node.js
npm
```

### Installation

1. Clone the repository
```bash
git clone git@github.com:Michael23Magdy/ThreadLine.git
```

2. Backend Setup
```bash
# Switch to implementation branch
cd backend
mvn clean install
mvn spring-boot:run
```

3. Frontend Setup
```bash
# Switch to frontend branch
cd frontend
npm install
npm start
```

## 👥 Team

| Name            | Role                    |
| --------------- | ----------------------- |
| [Huthaifa Omar](https://github.com/HuzaifaOmar)   | Backend Development     |
| [Michael Magdy](https://github.com/Michael23Magdy)   | Frontend Development    |
