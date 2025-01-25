# ThreadLine: Producer/Consumer Simulation System 🏭

<div align="center">


A real-time assembly line simulation system featuring multi-threaded processing, dynamic visualization, and interactive configuration.

[![Java](https://img.shields.io/badge/Java-21-red.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18.2.0-blue.svg)](https://reactjs.org/)


https://github.com/user-attachments/assets/51302356-f519-4316-bac8-fdfe13656971


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
    <source src="./media/DEMO.mp4" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</div>

</div>

## 🏗 Architecture

### System Design (UML Class Diagram)

<details>
<summary>Click to expand UML Diagram</summary>
<div align="center">
    <img src="./out/backend/UML-Diagram/UML-Diagram.svg" alt="UML Diagram">
</div>

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
npm run dev
```

## 👥 Team

| Name            | Role                    |
| --------------- | ----------------------- |
| [Huthaifa Omar](https://github.com/HuzaifaOmar)   | Backend Development     |
| [Michael Magdy](https://github.com/Michael23Magdy)   | Frontend Development    |
